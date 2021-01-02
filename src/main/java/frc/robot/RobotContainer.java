package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;
import frc.robot.oi.shufHELLboardDriver;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ClimberArm.Sides;
import frc.robot.lists.Colors;
import frc.robot.lists.LEDPrioritys;
import frc.robot.lists.Ports;
import frc.robot.lists.StatusPrioritys;
import frc.robot.commands.climb.ClimbSequence;
import frc.robot.commands.climb.ClimberArmMO;
import frc.robot.commands.conveyor.ConveyorAutomation;
import frc.robot.commands.conveyor.ConveyorMO;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.homing.HomeByCurrent;
import frc.robot.commands.intake.IntakeArmDefault;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetUp;
import frc.robot.commands.pathfollowing.GenerateRecording;
import frc.robot.commands.pathfollowing.PlayRecording;
import frc.robot.commands.turret.FullAutoShooterAssembly;
import frc.robot.commands.turret.FullManualShootingAssembly;
import frc.robot.commands.turret.SemiAutoShooterAssembly;
import frc.robot.commands.turret.TurretToPosition;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lidar;
import frc.robot.devices.LidarV3;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private CommandScheduler scheduler;

    private ControllerDriver controller1;
    private shufHELLboardDriver shufHELLboard;
    private LaunchpadDriver launchpad;
    private JoystickDriver joystick;

    private PowerDistributionPanel pdp;

    private Drivetrain drivetrain;
    private Shifter shifter;
    private Conveyor conveyor;
    private IntakeArm intakeArm;
    private Shooter shooter;
    private Hood hood;
    private ClimberArm leftArm, rightArm;
    private Turret turret;
    private ClimberPneumatics climberPneumatics;
    private Pneumatics pneumatics;

    // private Lemonlight limelight;
    private ColorSensorV3 colorSensor;
    private Lemonlight limelight;
    private Lidar turretLidar;
    private AHRS gyro;

    private Command autoInit;
    private Command teleInit;

    private HomeByCurrent HomeTurret;
    private HomeByCurrent HomeHood;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();

        controller1 = new ControllerDriver(Ports.XBOX_PORT);
        shufHELLboard = new shufHELLboardDriver();
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT);

        LEDs.getInstance().addCall("disabled", new LEDCall(LEDPrioritys.on, LEDRange.All).solid(Colors.DimGreen));
        shufHELLboard.statusDisplay.addStatus("deafult", "robot on", Colors.White, StatusPrioritys.on);

        //wpilib parts
        pdp = new PowerDistributionPanel();

        //our subsystems
        pneumatics = new Pneumatics(shufHELLboard.pressure);
        drivetrain = new Drivetrain();
        shifter = new Shifter();
        conveyor = new Conveyor();
        intakeArm = new IntakeArm();
        shooter = new Shooter(shufHELLboard.shooterSpeed, shufHELLboard.shooterTemp, shufHELLboard.statusDisplay);
        hood = new Hood(shufHELLboard.hoodIndicator);
        leftArm = new ClimberArm(Sides.LEFT);
        rightArm = new ClimberArm(Sides.RIGHT);
        turret = new Turret(shufHELLboard.turretIndicator);
        climberPneumatics = new ClimberPneumatics();


        SmartDashboard.putData(pdp);

        gyro = new AHRS();
        limelight = new Lemonlight();
        turretLidar = new LidarV3();
        //colorSensor = new ColorSensorV3(Port.kOnboard);

        HomeTurret = new HomeByCurrent(turret, -.2, 25, 2, 27);
        HomeHood = new HomeByCurrent(hood, -.15, 20, 2.5, 11.5);

        // autoInit = new SequentialCommandGroup(new InstantCommand(climberPneumatics::extendClimb),
        //         new InstantCommand(shifter::lowGear));

        // things that happen when the robot is inishlided


        teleInit = new SequentialCommandGroup(
            new InstantCommand(() ->  LEDs.getInstance().addCall("enabled", new LEDCall(LEDPrioritys.enabled, LEDRange.All).solid(Colors.Green))),
            new InstantCommand(() -> shufHELLboard.statusDisplay.addStatus("enabled", "robot enabled", Colors.Team, StatusPrioritys.enabled)),
            new InstantCommand(climberPneumatics::extendClimb),
            new InstantCommand(intakeArm::closeLock),
            new InstantCommand(shifter::highGear),
            new InstantCommand(() -> {
                launchpad.bigLEDRed.set(false);
                launchpad.bigLEDGreen.set(true);
            }),
            new InstantCommand(() -> conveyor.disableIntakeMode()),
            new InstantCommand(() -> conveyor.disableShootMode()),
            //these can both happen at the same time so we do want that to happen to save time
            new ParallelCommandGroup(HomeTurret.getDuplicate(), HomeHood.getDuplicate()) 
            );


        //these should always be the last things to run in the constructure
        setDefaultCommands();
        configureButtonBindings();
    }

    private void setDefaultCommands() {
        // drive by controler
        drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain, controller1.rightTrigger,
                controller1.leftTrigger, controller1.leftX));

        // makes intake arm go back to limit when not on limit
        intakeArm.setDefaultCommand(new IntakeArmDefault(intakeArm));

        conveyor.setDefaultCommand(new ConveyorAutomation(conveyor));

    }

    private void configureButtonBindings() {

        //shufHELLboard.recordStart.whenPressed(new GenerateRecording(drivetrain, shifter, intakeArm, controller1.buttonA, shufHELLboard.finish, shufHELLboard.shift, shufHELLboard.intake));
        // Launchpad bindings

        //controller1.buttonB.whenPressed(new PlayRecording(scheduler, "test1.chs", drivetrain, shifter, intakeArm, allLEDS));
        
        //climb
        launchpad.missileA.whenPressed(new ClimbSequence(leftArm, rightArm, climberPneumatics, launchpad.axisA,
        launchpad.axisB, launchpad.missileA, launchpad.bigLEDGreen, launchpad.bigLEDRed));

        launchpad.buttonA.whenPressed(
            new InstantCommand(
                climberPneumatics::toggleClimb
            )
        );
        launchpad.buttonA.booleanSupplierBind(climberPneumatics::getClimbState);

        //moes
        launchpad.buttonB.whileActiveContinuous(new ClimberArmMO(rightArm, joystick.axisY), false);
        launchpad.buttonB.pressBind();

        launchpad.buttonC.whileActiveContinuous(new ClimberArmMO(leftArm, joystick.axisY), false);
        launchpad.buttonC.pressBind();

        launchpad.buttonD.whenPressed(
            new InstantCommand(conveyor::toggleIntakeMode)
        );
        launchpad.buttonD.booleanSupplierBind(conveyor::getIntakeMode);

        launchpad.buttonE.whileActiveContinuous(new ConveyorMO(conveyor, joystick.axisY), false);
        launchpad.buttonE.pressBind();

        launchpad.buttonF.whileActiveContinuous(new IntakeArmMO(intakeArm, joystick.axisY, joystick.trigger, joystick.button3, joystick.button2), false);
        launchpad.buttonF.pressBind();

        shufHELLboard.homeTurret.whenPressed(HomeTurret);
        shufHELLboard.homeTurret.commandBind(HomeTurret);

        shufHELLboard.homeHood.whenPressed(HomeHood);
        shufHELLboard.homeHood.commandBind(HomeHood);

        //intake arm

        Command intake = new SequentialCommandGroup(
            new InstantCommand(() -> conveyor.enableIntakeMode()),
            new SetDown(intakeArm)
            );

        Command up = new SequentialCommandGroup(
            new InstantCommand(() -> conveyor.disableIntakeMode()),
            new SetUp(intakeArm)
            );

        
        launchpad.buttonH.whenPressed(intake, false);
        launchpad.buttonH.booleanSupplierBind(intakeArm::isDown);

        launchpad.buttonI.whenPressed(up, false);
        launchpad.buttonI.booleanSupplierBind(intakeArm::isUp);

        //bindings for fun dile
        launchpad.funLeft.whenPressed(new FullManualShootingAssembly(turret, shooter, hood, conveyor, joystick.axisX, joystick.axisZ, joystick.axisY, joystick.trigger));
        launchpad.funMiddle.whenPressed(new SemiAutoShooterAssembly(scheduler, turret, shooter, hood, conveyor, limelight, turretLidar, shufHELLboard.statusDisplay, joystick.axisX, joystick.trigger));
        launchpad.funRight.whenPressed(new FullAutoShooterAssembly(scheduler, turret, shooter, hood, conveyor, limelight, turretLidar, shufHELLboard.statusDisplay));

        //Controller bindings for intake
        controller1.buttonX.whenPressed(up, false);
        controller1.buttonA.whenPressed(intake, false);

        //shifting
        controller1.leftBumper.toggleWhenPressed(new StartEndCommand(
            () -> {
                shifter.lowGear();
                launchpad.bigLEDRed.set(true);
                launchpad.bigLEDGreen.set(false);
            }, () -> {
                shifter.highGear();
                launchpad.bigLEDRed.set(false);
                launchpad.bigLEDGreen.set(true);
            }, shifter
        ));

    }

    public void disabledInit(){
        LEDs.getInstance().removeCall("enabled");
        shufHELLboard.statusDisplay.removeStatus("enabled");
    }

    /**
     * runs when robot is inited to telyop
     */
    public void teleopInit() {
        //inishlises robot
        scheduler.schedule(teleInit);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    // public Command getAutonomousCommand() {
    //     return new SequentialCommandGroup(
    //         autoInit,
    //         new EncoderDrive(drivetrain, 50)
    //     );
    // }
}

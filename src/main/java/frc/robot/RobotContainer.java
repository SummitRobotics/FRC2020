package frc.robot;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;
import frc.robot.oi.shufHELLboardDriver;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ClimberArm.Sides;
import frc.robot.utilities.Colors;
import frc.robot.utilities.Ports;
import frc.robot.commands.climb.ClimbSequence;
import frc.robot.commands.climb.ClimberArmMO;
import frc.robot.commands.conveyor.ConveyorAutomation;
import frc.robot.commands.conveyor.ConveyorMO;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.drivetrain.EncoderDrive;
import frc.robot.commands.homing.HomeByCurrent;
import frc.robot.commands.intake.IntakeArmDefault;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetUp;
import frc.robot.commands.pathfollowing.GenerateRecording;
import frc.robot.commands.pathfollowing.PlayRecording;
import frc.robot.commands.shooter.ShooterTester;
import frc.robot.commands.turret.FullManualShootingAssembly;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.PresureSensor;

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

    private Compressor compressor;

    private Drivetrain drivetrain;
    private Shifter shifter;
    private Conveyor conveyor;
    private IntakeArm intakeArm;
    private Shooter shooter;
    private Hood hood;
    private ClimberArm leftArm, rightArm;
    private Turret turret;
    private ClimberPneumatics climberPneumatics;

    // private Lemonlight limelight;
    private ColorSensorV3 colorSensor;
    private Lemonlight limelight;
    private PresureSensor presureSensor;

    private Command autoInit;
    private Command teleInit;
    private Command homeTurret;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();

        LEDs.getInstance().addCall("deafult", new LEDCall(1, LEDRange.All).solid(Colors.Green));

        controller1 = new ControllerDriver(Ports.XBOX_PORT);
        shufHELLboard = new shufHELLboardDriver();
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT);

        compressor = new Compressor(Ports.PCM_1);
        compressor.setClosedLoopControl(true);

        drivetrain = new Drivetrain();
        shifter = new Shifter();
        conveyor = new Conveyor();
        intakeArm = new IntakeArm();
        shooter = new Shooter();
        hood = new Hood();
        leftArm = new ClimberArm(Sides.LEFT);
        rightArm = new ClimberArm(Sides.RIGHT);
        turret = new Turret();
        climberPneumatics = new ClimberPneumatics();

        //gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
        limelight = new Lemonlight();
        presureSensor = new PresureSensor();
        //colorSensor = new ColorSensorV3(Port.kOnboard);

        setDefaultCommands();
        configureButtonBindings();

        // autoInit = new SequentialCommandGroup(new InstantCommand(climberPneumatics::extendClimb),
        //         new InstantCommand(shifter::lowGear));

        // things that happen when the robot is inishlided

        homeTurret = new HomeByCurrent(turret, -.2, 25, 2, 27);

        teleInit = new SequentialCommandGroup(
                new InstantCommand(climberPneumatics::extendClimb),
                new InstantCommand(intakeArm::closeLock),
                new InstantCommand(shifter::highGear),
                new InstantCommand(() -> {
                    launchpad.bigLEDRed.set(false);
                    launchpad.bigLEDGreen.set(true);
                }),
                new InstantCommand(() -> conveyor.disableIntakeMode()),
                new InstantCommand(() -> conveyor.disableShootMode()),
                homeTurret
                );
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

        //launchpad.buttonD.whenPressed(new ShooterTester(shooter));

        launchpad.buttonE.whileActiveContinuous(new ConveyorMO(conveyor, joystick.axisY), false);
        launchpad.buttonE.pressBind();

        launchpad.buttonF.whileActiveContinuous(new IntakeArmMO(intakeArm, joystick.axisY, joystick.trigger, joystick.button3, joystick.button2), false);
        launchpad.buttonF.pressBind();

        Command reHomeTurret = new HomeByCurrent(turret, -.15, 22, 2, 27);

        launchpad.buttonG.whenPressed(reHomeTurret);

        launchpad.buttonG.booleanSupplierBind(reHomeTurret::isFinished);;

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

        // launchpad.funRight.whenPressed(new PrintCommand("maximum f u n :)"));
        // launchpad.funMiddle.whenPressed(new PrintCommand("medium fun :|"));
        // launchpad.funLeft.whenPressed(new PrintCommand("no fun T^T"));

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

        turret.setDefaultCommand(new FullManualShootingAssembly(
            turret,
            shooter,
            hood,
            conveyor,
            joystick.axisX,
            joystick.axisZ,
            joystick.axisY,
            joystick.trigger
        ));
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

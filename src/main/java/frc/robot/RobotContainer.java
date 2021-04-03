package frc.robot;

import java.io.IOException;
import java.nio.file.Path;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ClimberArm.Sides;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;
import frc.robot.utilities.lists.Ports;
import frc.robot.utilities.lists.StatusPrioritys;
import frc.robot.oi.Konami;
import frc.robot.oi.drivers.ControllerDriver;
import frc.robot.oi.drivers.JoystickDriver;
import frc.robot.oi.drivers.LaunchpadDriver;
import frc.robot.oi.drivers.ShufhellboardDriver;
import frc.robot.commands.climb.ClimbSequence;
import frc.robot.commands.climb.ClimberArmMO;
import frc.robot.commands.conveyor.ConveyorAutomation;
import frc.robot.commands.conveyor.ConveyorMO;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.drivetrain.EncoderDrive;
import frc.robot.commands.drivetrain.FollowTrajectoryThreaded;
import frc.robot.commands.drivetrain.FollowSpline;
import frc.robot.commands.drivetrain.FollowTrajectory;
import frc.robot.commands.homing.HomeByCurrent;
import frc.robot.commands.homing.HomeByEncoder;
import frc.robot.commands.intake.IntakeArmDefault;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetUp;
import frc.robot.commands.shooter.SpoolOnTarget;
import frc.robot.commands.turret.FullAutoShooterAssembly;
import frc.robot.commands.turret.FullManualShootingAssembly;
import frc.robot.commands.turret.SemiAutoShooterAssembly;
import frc.robot.commands.turret.TurretToPosition;
import frc.robot.commands.turret.VisionTarget;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.devices.Lemonlight.LEDModes;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.Lidar;
import frc.robot.devices.LidarLight;
import frc.robot.devices.LidarV3;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    // for science!

    private CommandScheduler scheduler;

    private ControllerDriver controller1;
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

    private Lemonlight limelight;
    private Lidar turretLidar;
    private LidarLight lidarlight;
    private AHRS gyro;

    private Command autoInit;
    private Command teleInit;

    private HomeByEncoder HomeTurret;
    private HomeByCurrent HomeHood;

    Command testSpline;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();

        controller1 = new ControllerDriver(Ports.XBOX_PORT);
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT);

        new LEDCall("disabled", LEDPriorities.on, LEDRange.All).solid(Colors.DimGreen).activate();
        ShufhellboardDriver.statusDisplay.addStatus("default", "robot on", Colors.White, StatusPrioritys.on);

        gyro = new AHRS();
        limelight = new Lemonlight();
        turretLidar = new LidarV3();
        lidarlight = new LidarLight(limelight, turretLidar);

        //wpilib parts
        pdp = new PowerDistributionPanel();

        //our subsystems
        pneumatics = new Pneumatics(ShufhellboardDriver.pressure);
        shifter = new Shifter();
        conveyor = new Conveyor();
        intakeArm = new IntakeArm();
        shooter = new Shooter(ShufhellboardDriver.shooterSpeed, ShufhellboardDriver.shooterTemp, ShufhellboardDriver.statusDisplay);
        hood = new Hood(ShufhellboardDriver.hoodIndicator);
        leftArm = new ClimberArm(Sides.LEFT);
        rightArm = new ClimberArm(Sides.RIGHT);
        turret = new Turret(ShufhellboardDriver.turretIndicator);
        climberPneumatics = new ClimberPneumatics();
        drivetrain = new Drivetrain(gyro, () -> shifter.getShiftState());

        
        //HomeTurret = new HomeByCurrent(turret, -.2, 26, 2, 27);
        HomeHood = new HomeByCurrent(hood, -.15, 20, 2.5, 10.5);

        HomeTurret = new HomeByEncoder(turret, -0.2, 20, 2, 27);

        // autoInit = new SequentialCommandGroup(new InstantCommand(climberPneumatics::extendClimb),
        //         new InstantCommand(shifter::lowGear));

        // things that happen when the robot is inishlided
        teleInit = new SequentialCommandGroup(
            new InstantCommand(() -> ShufhellboardDriver.statusDisplay.addStatus("enabled", "robot enabled", Colors.Team, StatusPrioritys.enabled)),
            new InstantCommand(() -> joystick.ReEnableJoysticCalibrationCheck()),
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
            new ParallelCommandGroup(HomeTurret.getDuplicate(), HomeHood.getDuplicate()),
            new TurretToPosition(turret, 90),
            //for tuning turret pid
            // new HoodToAngle(hood, 20),
            // sets the turret default command based on the fun dile
            new InstantCommand(() -> {  
                try {
                    turret.getDefaultCommand().cancel();
                } catch(NullPointerException e) {
                }
                if (launchpad.funLeft.get()) {
                    turret.setDefaultCommand(new FullManualShootingAssembly(turret, shooter, hood, conveyor, joystick.axisX, joystick.axisZ, joystick.axisY, joystick.trigger));
                } else if (launchpad.funMiddle.get()) {
                    turret.setDefaultCommand(new SemiAutoShooterAssembly(turret, shooter, hood, conveyor, lidarlight, ShufhellboardDriver.statusDisplay, joystick.axisX, joystick.trigger, launchpad.buttonD));
                } else if (launchpad.funRight.get()) {
                    turret.setDefaultCommand(new FullAutoShooterAssembly(turret, shooter, hood, conveyor, lidarlight, ShufhellboardDriver.statusDisplay, launchpad.buttonD, joystick.axisX));
                }
            })
            );


        //these should always be the last things to run in the constructure
        setDefaultCommands();
        configureButtonBindings();
    }

    private void setDefaultCommands() {
        // drive by controler
        drivetrain.setDefaultCommand(new ArcadeDrive(
            drivetrain, 
            controller1.rightTrigger,
            controller1.leftTrigger, 
            controller1.leftX));

        // makes intake arm go back to limit when not on limit
        intakeArm.setDefaultCommand(new IntakeArmDefault(intakeArm));

        conveyor.setDefaultCommand(new ConveyorAutomation(conveyor));

    }

    private void configureButtonBindings() {

        //shufHELLboard.recordStart.whenPressed(new GenerateRecording(drivetrain, shifter, intakeArm, controller1.buttonA, shufHELLboard.finish, shufHELLboard.shift, shufHELLboard.intake));
        // Launchpad bindings

        //controller1.buttonB.whenPressed(new PlayRecording(scheduler, "test1.chs", drivetrain, shifter, intakeArm, allLEDS));
        
        //climb
        launchpad.missileA.whenPressed(new ClimbSequence(
            leftArm, 
            rightArm, 
            climberPneumatics, 
            launchpad.axisA,
            launchpad.axisB, 
            launchpad.missileA, 
            launchpad.bigLEDGreen, 
            launchpad.bigLEDRed));

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

        // launchpad.buttonD.whenPressed(
        //     new InstantCommand(conveyor::toggleIntakeMode)
        // );
        // launchpad.buttonD.booleanSupplierBind(conveyor::getIntakeMode);

        // Command testSpline = new FollowSavedTrajectoryThreaded(drivetrain, "/home/admin/splines/spline.spl");
        // Command testSpline = new FollowSavedTrajectoryScheduledExecuter(drivetrain, "not currently relevant");
        

        launchpad.buttonE.whileActiveContinuous(new ConveyorMO(conveyor, joystick.axisY), false);
        launchpad.buttonE.pressBind();

        launchpad.buttonF.whileActiveContinuous(new IntakeArmMO(intakeArm, joystick.axisY, joystick.trigger, joystick.button3, joystick.button2), false);
        launchpad.buttonF.pressBind();

        ShufhellboardDriver.homeTurret.whenPressed(HomeTurret);
        ShufhellboardDriver.homeTurret.commandBind(HomeTurret);

        ShufhellboardDriver.homeHood.whenPressed(HomeHood);
        ShufhellboardDriver.homeHood.commandBind(HomeHood);

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

        // launchpad.buttonG.whenHeld(new StartEndCommand(
        //     () -> shooter.setCoolerSolenoid(true), 
        //     () -> shooter.setCoolerSolenoid(false)));
        // launchpad.buttonG.pressBind();

        // launchpad.buttonG.toggleWhenPressed(new VisionTarget(turret, limelight, false) {
        //     @Override
        //     protected double noTargetTurretAction(double turretAngle) {
        //         return joystick.axisX.get();
        //     };
        // });
        // launchpad.buttonG.toggleBind();

        // bindings for fun dial
        launchpad.funLeft.whenPressed(new InstantCommand(() -> {
            turret.getDefaultCommand().cancel();
            turret.setDefaultCommand(new FullManualShootingAssembly(turret, shooter, hood, conveyor, joystick.axisX, joystick.axisZ, joystick.axisY, joystick.trigger));
        }));
        launchpad.funMiddle.whenPressed(new InstantCommand(() -> {
            turret.getDefaultCommand().cancel();
            turret.setDefaultCommand(new SemiAutoShooterAssembly(turret, shooter, hood, conveyor, lidarlight, ShufhellboardDriver.statusDisplay, joystick.axisX, joystick.trigger, launchpad.buttonD));
        }));
        launchpad.funRight.whenPressed(new InstantCommand(() -> {
            turret.getDefaultCommand().cancel();
            turret.setDefaultCommand(new FullAutoShooterAssembly(turret, shooter, hood, conveyor, lidarlight, ShufhellboardDriver.statusDisplay, launchpad.buttonD, joystick.axisX));
        }));

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


        // secret!
        LEDCall secretLEDs = new LEDCall(Integer.MAX_VALUE, LEDRange.All).rainbow();
        Konami.getInstance().addSequence(
            new InstantCommand(
                () -> {
                    System.out.println("there should be rainbows");
                    secretLEDs.activate();
                }
            ),
            "up", "up", "down", "down", "left", "right", "left", "right", "b", "a", "start"
        );

        Konami.getInstance().addSequence(
            new InstantCommand(secretLEDs::cancel), 
            "b", "b", "b", "b", "b", "b", "b", "b");
    }

    public void disabledInit(){
        LEDs.getInstance().removeAllCalls();
        new LEDCall("disabled", LEDPriorities.on, LEDRange.All).solid(Colors.DimGreen).activate();
        ShufhellboardDriver.statusDisplay.removeStatus("enabled");
    }

    public void teleopPeriodic(){

    }

    /**
     * runs when robot is inited to telyop
     */
    public void teleopInit() {
        // initialises robot
        // for testing ONLY
        //scheduler.schedule(testSpline);

        scheduler.schedule(teleInit);

        // scheduler.schedule(new SpoolOnTarget(shooter, lidarlight));
       
    }

    public void robotInit(){
        String path1 = "paths/test.wpilib.json";
        String path2 = "paths/garbo-auto-path-2.wpilib.json";

        Trajectory trajectory1 = new Trajectory();
        Trajectory trajectory2 = new Trajectory();

        try {
            Path trajectoryPath1 = Filesystem.getDeployDirectory().toPath().resolve(path1);
            trajectory1 = TrajectoryUtil.fromPathweaverJson(trajectoryPath1);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + path1, ex.getStackTrace());
        }

        try {
            Path trajectoryPath2 = Filesystem.getDeployDirectory().toPath().resolve(path2);
            trajectory2 = TrajectoryUtil.fromPathweaverJson(trajectoryPath2);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + path2, ex.getStackTrace());
        }

        testSpline = new FollowTrajectory(drivetrain, trajectory1);
        // testSpline = new SequentialCommandGroup(
        //     new FollowTrajectoryThreaded(drivetrain, trajectory1),
        //     new FollowTrajectoryThreaded(drivetrain, trajectory2)
        // );
        // launchpad.buttonD.whenPressed(testSpline);
        // launchpad.buttonD.commandBind(testSpline);
        // launchpad.buttonG.whenPressed(new InstantCommand(() -> testSpline.cancel()));
        launchpad.buttonD.pressBind();

        // launchpad.buttonD.toggleWhenPressed(new RunCommand(() -> {
        //     SmartDashboard.putNumber("Lidar distance", turretLidar.getCompensatedLidarDistance(turretLidar.getDistance()));
        // }));
        // launchpad.buttonD.toggleWhenPressed(new RunCommand(() -> {
        //     SmartDashboard.putNumber("Limelight distance", limelight.getLimelightDistanceEstimateIN(limelight.getVerticalOffset()));
        // }));
        // launchpad.buttonD.toggleBind();
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new SequentialCommandGroup(
            teleInit,
            // new FollowTrajectory(drivetrain, "paths/garbo-auto-path-1.wpilib.json"),
            new InstantCommand(() -> drivetrain.stop()),
            new InstantCommand(() -> conveyor.enableIntakeMode()),
            new SetDown(intakeArm),
            new WaitCommand(1),
            new EncoderDrive(drivetrain, 25, 25),
            new InstantCommand(() -> conveyor.disableIntakeMode()),
            new SetUp(intakeArm),
            new FollowSpline(drivetrain),
            new InstantCommand(() -> drivetrain.stop())
        );
    }
}

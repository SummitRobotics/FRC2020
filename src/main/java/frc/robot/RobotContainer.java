package frc.robot;

import java.io.IOException;
import java.nio.file.Path;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
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
import frc.robot.commands.CommandThreader;
import frc.robot.commands.climb.ClimbSequence;
import frc.robot.commands.climb.ClimberArmMO;
import frc.robot.commands.conveyor.ConveyorAutomation;
import frc.robot.commands.conveyor.ConveyorMO;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.drivetrain.FollowTrajectoryThreaded;
import frc.robot.commands.homing.HomeByCurrent;
import frc.robot.commands.homing.HomeByEncoder;
import frc.robot.commands.intake.IntakeArmDefault;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetUp;
import frc.robot.commands.turret.FullAutoShooterAssembly;
import frc.robot.commands.turret.FullManualShootingAssembly;
import frc.robot.commands.turret.SemiAutoShooterAssembly;
import frc.robot.commands.turret.TurretToPosition;
import frc.robot.devices.LEDs.LEDs;
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

    private Command
    Test = new PrintCommand("unlimited badness");


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
        intakeArm = new IntakeArm(pdp);
        shooter = new Shooter(ShufhellboardDriver.shooterSpeed, ShufhellboardDriver.shooterTemp, ShufhellboardDriver.statusDisplay);
        hood = new Hood(ShufhellboardDriver.hoodIndicator);
        leftArm = new ClimberArm(Sides.LEFT);
        rightArm = new ClimberArm(Sides.RIGHT);
        turret = new Turret(ShufhellboardDriver.turretIndicator);
        climberPneumatics = new ClimberPneumatics();
        drivetrain = new Drivetrain(gyro, () -> shifter.getShiftState());

        
        //HomeTurret = new HomeByCurrent(turret, -.2, 26, 2, 27);
        HomeHood = new HomeByCurrent(hood, -.15, 15, 2.5, 10.5, 4);

        HomeTurret = new HomeByEncoder(turret, -0.2, 20, 7, 27, 4);

        //things the robot does to make auto work
        autoInit = new SequentialCommandGroup(
            new InstantCommand(() -> ShufhellboardDriver.statusDisplay.addStatus("auto", "robot in auto", Colors.Team, StatusPrioritys.enabled)),
            new InstantCommand(climberPneumatics::extendClimb),
            new InstantCommand(intakeArm::closeLock),
            new InstantCommand(shifter::highGear),
            new InstantCommand(() -> {
                launchpad.bigLEDRed.set(false);
                launchpad.bigLEDGreen.set(true);
            }),
            new InstantCommand(() -> conveyor.disableIntakeMode()),
            new InstantCommand(() -> conveyor.disableShootMode())
            );

        // things that happen when the robot is inishlided
        teleInit = new SequentialCommandGroup(
            new InstantCommand(() -> {
                if(!controller1.isConnected() || !launchpad.isConnected() || !joystick.isConnected()){
                    System.out.println("not enough joysticks connected, plaease make sure the xbox controler, launchpad, and joystics are connected to the driverstation");
                    throw(new RuntimeException("not enough joysticks connected"));
                }
        
                if(!controller1.isXboxControler()){
                    System.out.println("controler 0 is not the xbox controler");
                    throw(new RuntimeException("incorect joystick in port 0"));
                }
            }),
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
            //new InstantCommand(() -> System.out.println("got halfway")),
            //these can both happen at the same time so we do want that to happen to save time
            new ParallelCommandGroup(HomeTurret.getDuplicate(), HomeHood.getDuplicate()),
            new TurretToPosition(turret, 90),
            //new InstantCommand(() -> System.out.println("homed")),
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
        // String path1 = "paths/test.wpilib.json";
        // String path2 = "paths/garbo-auto-path-2.wpilib.json";

        // Trajectory trajectory1 = new Trajectory();
        // Trajectory trajectory2 = new Trajectory();

        // try {
        //     Path trajectoryPath1 = Filesystem.getDeployDirectory().toPath().resolve(path1);
        //     trajectory1 = TrajectoryUtil.fromPathweaverJson(trajectoryPath1);
        // } catch (IOException ex) {
        //     DriverStation.reportError("Unable to open trajectory: " + path1, ex.getStackTrace());
        // }

        // try {
        //     Path trajectoryPath2 = Filesystem.getDeployDirectory().toPath().resolve(path2);
        //     trajectory2 = TrajectoryUtil.fromPathweaverJson(trajectoryPath2);
        // } catch (IOException ex) {
        //     DriverStation.reportError("Unable to open trajectory: " + path2, ex.getStackTrace());
        // }

        // testSpline = new FollowTrajectory(drivetrain, trajectory1);

        String
        pathF2 = "paths/f2.wpilib.json",
        pathF3 = "paths/f3.wpilib.json";

        try {
            Path loadedPathSlalom = Filesystem.getDeployDirectory().toPath().resolve(pathF2);
            Path loadedPathBarrel = Filesystem.getDeployDirectory().toPath().resolve(pathF3);

            Trajectory trajectorySlalom = TrajectoryUtil.fromPathweaverJson(loadedPathSlalom);
            Trajectory trajectoryBarrel = TrajectoryUtil.fromPathweaverJson(loadedPathBarrel);

            Command f2 = new FollowTrajectoryThreaded(drivetrain, trajectorySlalom);
            Command f3 = new FollowTrajectoryThreaded(drivetrain, trajectoryBarrel);

            Test = new SequentialCommandGroup(f2, f3);

            ShufhellboardDriver.autoChooser.addOption("testAuto", Test);
            
        } catch (IOException ex) {
            DriverStation.reportError("Unable to get auto paths", ex.getStackTrace());
        }

        //TODO move to shufhellboard driver
        SmartDashboard.putData(ShufhellboardDriver.autoChooser);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
     
        return (Command) ShufhellboardDriver.autoChooser.getSelected();
    }

}

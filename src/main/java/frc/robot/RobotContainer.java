package frc.robot;

import java.io.IOException;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.*;
import frc.robot.subsystems.ClimberArm.Sides;
import frc.robot.utilities.Functions;
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

    private FullAutoShooterAssembly fullAutoShooting;
    private Command semiAutoShooting;
    private Command fullManualShooting;

    private Command intake;
    private Command up;
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
        HomeHood = new HomeByCurrent(hood, -.15, 15, hood.backLimit, hood.fowardLimit, 4);

        HomeTurret = new HomeByEncoder(turret, -0.2, 20, turret.shootingBackLimit, turret.fowardLimit, 4);

        fullAutoShooting = new FullAutoShooterAssembly(turret, shooter, hood, conveyor, lidarlight, ShufhellboardDriver.statusDisplay);

        semiAutoShooting = new SemiAutoShooterAssembly(turret, shooter, hood, conveyor, lidarlight, ShufhellboardDriver.statusDisplay, joystick.axisX, joystick.trigger);

        fullManualShooting = new FullManualShootingAssembly(turret, shooter, hood, conveyor, joystick.axisX, joystick.axisZ, joystick.axisY, joystick.trigger);

        intake = new SequentialCommandGroup(
            new InstantCommand(() -> conveyor.enableIntakeMode()),
            new SetDown(intakeArm)
        );

        up = new SequentialCommandGroup(
            new InstantCommand(() -> conveyor.disableIntakeMode()),
            new SetUp(intakeArm)
        );

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
            new ParallelCommandGroup(HomeTurret.getDuplicate(), HomeHood.getDuplicate()),
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
            new InstantCommand(() -> ShufhellboardDriver.statusDisplay.removeStatus("auto")),
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
                    turret.setDefaultCommand(fullManualShooting);
                } else if (launchpad.funMiddle.get()) {
                    turret.setDefaultCommand(semiAutoShooting);
                } else if (launchpad.funRight.get()) {
                    turret.setDefaultCommand(fullAutoShooting);
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
            controller1.leftX
        ));
        
        // makes intake arm go back to limit when not on limit
        intakeArm.setDefaultCommand(new IntakeArmDefault(intakeArm));

        //makes the convayer run
        conveyor.setDefaultCommand(new ConveyorAutomation(conveyor));

    }

    private void configureButtonBindings() {

        //shufHELLboard.recordStart.whenPressed(new GenerateRecording(drivetrain, shifter, intakeArm, controller1.buttonA, shufHELLboard.finish, shufHELLboard.shift, shufHELLboard.intake));
        // Launchpad bindings

        //controller1.buttonB.whenPressed(new PlayRecording(scheduler, "test1.chs", drivetrain, shifter, intakeArm, allLEDS));
        
        //climb
        launchpad.missileA.whenPressed(new ParallelCommandGroup(
                new ClimbSequence(
                    leftArm, 
                    rightArm, 
                    climberPneumatics, 
                    launchpad.axisA,
                    launchpad.axisB, 
                    launchpad.missileA, 
                    launchpad.bigLEDGreen, 
                    launchpad.bigLEDRed), 
                new TurretToPosition(turret, 90)));

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

        //buttons to home on shufflebaord
        ShufhellboardDriver.homeTurret.whenPressed(HomeTurret);
        ShufhellboardDriver.homeTurret.commandBind(HomeTurret);

        ShufhellboardDriver.homeHood.whenPressed(HomeHood);
        ShufhellboardDriver.homeHood.commandBind(HomeHood);


        //launchpad intake arm buttons
        launchpad.buttonH.whenPressed(intake, false);
        launchpad.buttonH.booleanSupplierBind(intakeArm::isDown);

        launchpad.buttonI.whenPressed(up, false);
        launchpad.buttonI.booleanSupplierBind(intakeArm::isUp);

        //toggle to stow te turret while moving
        Command turretToPos = new TurretToPosition(turret, 12).perpetually();

        Command turretStow = new StartEndCommand(
            () -> {turret.setSoftLimits(turret.normalBackLimit, turret.fowardLimit); scheduler.schedule(true, turretToPos);}, 
            () -> {scheduler.cancel(turretToPos); turret.setSoftLimits(turret.shootingBackLimit, turret.fowardLimit);}, 
            turret, shooter, hood, conveyor);

        launchpad.buttonG.toggleWhenPressed(turretStow);
        launchpad.buttonG.commandBind(turretStow);


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
            turret.setDefaultCommand(fullManualShooting);
        }));
        launchpad.funMiddle.whenPressed(new InstantCommand(() -> {
            turret.getDefaultCommand().cancel();
            turret.setDefaultCommand(semiAutoShooting);
        }));
        launchpad.funRight.whenPressed(new InstantCommand(() -> {
            turret.getDefaultCommand().cancel();
            turret.setDefaultCommand(fullAutoShooting);
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

    /**
     * runs when the robot gets disabled
     */
    public void disabledInit(){
        LEDs.getInstance().removeAllCalls();
        new LEDCall("disabled", LEDPriorities.on, LEDRange.All).solid(Colors.DimGreen).activate();
        ShufhellboardDriver.statusDisplay.removeStatus("enabled");
    }

    /**
     * runs once every ~20ms when in telyop
     */
    public void teleopPeriodic(){

    }

    /**
     * runs when robot is inited to telyop
     */
    public void teleopInit() {

        scheduler.schedule(teleInit);       
    }

    /**
     * runs when the robot is powered on
     */
    public void robotInit(){

        //sets up all the splines so we dont need to spend lots of time
        //turning the json files into trajectorys when we want to run them
        //TODO make this run faster somehow
        String
        pathF2 = "paths/f2.wpilib.json",
        pathF3 = "paths/f3.wpilib.json",
        pathLineMove = "paths/lineMove.wpilib.json",
        pathDriveBack = "paths/driveBack.wpilib.json",
        pathColectBalls = "paths/colectBalls.wpilib.json";

        try {
            Command f2 = Functions.splineCommandFromFile(drivetrain, pathF2);
            Command f3 = Functions.splineCommandFromFile(drivetrain, pathF3);
            Command lineDrive = Functions.splineCommandFromFile(drivetrain, pathLineMove);
            Command driveBack = Functions.splineCommandFromFile(drivetrain, pathDriveBack);
            Command getBals = Functions.splineCommandFromFile(drivetrain, pathColectBalls);

            Command auto = new SequentialCommandGroup(lineDrive, fullAutoShooting.getDuplicate());
            //                                                       makes the full auto shooting stop after 5s
            Command BallAuto = new SequentialCommandGroup(driveBack, fullAutoShooting.getDuplicate().withTimeout(5), new InstantCommand(() -> conveyor.enableIntakeMode()), new SetDown(intakeArm), getBals, fullAutoShooting.getDuplicate());

            Command Test = new SequentialCommandGroup(f2, f3);

            ShufhellboardDriver.autoChooser.setDefaultOption("auto", auto);
            ShufhellboardDriver.autoChooser.addOption("5Ball", BallAuto);
            ShufhellboardDriver.autoChooser.addOption("test Auto", Test);
            
        } catch (IOException ex) {
            DriverStation.reportError("Unable to get auto paths", ex.getStackTrace());
        }

        //inits shuffleboard
        ShufhellboardDriver.init();
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
     
        return new SequentialCommandGroup(autoInit,  (Command) ShufhellboardDriver.autoChooser.getSelected());
    }

}

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;
import frc.robot.subsystems.BuddyClimb;
import frc.robot.subsystems.ClimberArms;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shifter;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.utilities.Ports;
import frc.robot.commands.*;
import frc.robot.commands.climb.LeftClimberArmMO;
import frc.robot.commands.climb.RaiseArmsSync;
import frc.robot.commands.climb.RaiseLeftArm;
import frc.robot.commands.climb.RightClimberArmMO;
import frc.robot.commands.climb.TrimArms;
import frc.robot.commands.intake.IntakeArmDefault;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetLoad;
import frc.robot.commands.intake.SetUp;
import frc.robot.devices.LEDs;
import frc.robot.devices.PigeonGyro;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.Lemonlight;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private CommandScheduler scheduler;

    private SyncLogger logger;

    private ControllerDriver controller1;
    private LaunchpadDriver launchpad;
    private JoystickDriver joystick;

    private LEDs leds;
    private Compressor compressor;

    private Drivetrain drivetrain;
    private Shifter shifter;
    private Conveyor conveyor;
    private IntakeArm intakeArm;
    private Shooter shooter;
    private ClimberArms climber;
    private Turret turret;
    private BuddyClimb buddyClimb;

    private Lemonlight limelight;

    private DoubleSolenoid buddySolenoid;
    private Solenoid lock;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();
        logger = new SyncLogger();

        leds = new LEDs();
        LEDRange shifterRange = leds.getAllLedsRangeController();

        controller1 = new ControllerDriver(Ports.XBOX_PORT, logger);
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT, logger);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT, logger);

        compressor = new Compressor(Ports.PCM_1);
        compressor.setClosedLoopControl(true);

        drivetrain = new Drivetrain();
        shifter = new Shifter(shifterRange);
        //conveyor = new Conveyor();
        intakeArm = new IntakeArm();
        //shooter = new Shooter();
        climber = new ClimberArms();
        //turret = new Turret();
        //buddyClimb = new BuddyClimb(climber);

        //buddySolenoid = new DoubleSolenoid(Ports.PCM_1, Ports.OPEN_CLAMP, Ports.CLOSE_CLAMP);

        //gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
        //limelight = new Lemonlight();

        setDefaultCommands();

        logger.addElements(drivetrain, shifter);
        //scheduler.setDefaultCommand(logger, logger);

        configureButtonBindings();
    }

    private void setDefaultCommands() {
        /*
        climber.setDefaultCommand(new RunCommand(
            () -> {
                double power = controller1.rightY.get();
                power = Math.abs(power) < .07 ? 0 : power;

                climber.setPower(power);
            },
            climber
        ));
        */
        
        /*
        shooter.setDefaultCommand(new RunCommand(
            () -> shooter.setPower(controller1.rightTrigger.get()),
            shooter
        ));
        */
        
        drivetrain.setDefaultCommand(new ArcadeDrive(
            drivetrain, 
            shifter, 
            controller1.rightTrigger, 
            controller1.leftTrigger, 
            controller1.leftX
        ));
        
        intakeArm.setDefaultCommand(new IntakeArmDefault(intakeArm));

        //launchpad.buttonE.whileActiveOnce(new IntakeArmMO(intakeArm, controller1.leftY, controller1.rightBumper));
        //launchpad.buttonE.pressBind();
    }

    private void configureButtonBindings() {
        launchpad.buttonE.whileActiveContinuous(new IntakeArmMO(
            intakeArm, joystick.axisY, joystick.trigger));
        launchpad.buttonE.pressBind();

        controller1.buttonX.whenPressed(new SetUp(intakeArm));
        controller1.buttonA.whenPressed(new SetDown(intakeArm));
        controller1.buttonB.whenPressed(new SetLoad(intakeArm));

        Command hatemyself = new ParallelCommandGroup(
            new InstantCommand(climber::extendClimb),
            new RaiseLeftArm(climber, ClimberArms.LEFT_CONTROL_PANEL_POSITION)
        );

        launchpad.buttonH.whenPressed(hatemyself);
        launchpad.buttonH.commandBind(hatemyself);

        Command wanttodie = new ParallelCommandGroup(
            new InstantCommand(climber::extendClimb),
            new RaiseArmsSync(climber, ClimberArms.CLIMB_POSITION)
        );

        launchpad.buttonG.whenPressed(wanttodie);
        launchpad.buttonG.commandBind(wanttodie);

        launchpad.buttonI.whileActiveContinuous(new LeftClimberArmMO(climber, joystick.axisY));
        launchpad.buttonI.pressBind();

        launchpad.buttonF.whileActiveContinuous(new RightClimberArmMO(climber, joystick.axisY));
        launchpad.buttonF.pressBind();

        Command trim = new TrimArms(launchpad.axisA, launchpad.axisB, climber);
        launchpad.buttonD.whenPressed(trim, true);
        launchpad.buttonD.commandBind(trim);

        /*
        launchpad.buttonE.whenPressed(new EncoderDrive(drivetrain, 500));
        launchpad.buttonE.whenReleased(new EncoderDrive(drivetrain, -500));
        */
    }

    public void teleopInit() {
        scheduler.schedule(new SetUp(intakeArm));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return null;
    }
}

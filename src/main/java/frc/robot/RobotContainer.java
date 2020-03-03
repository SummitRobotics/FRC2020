package frc.robot;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;
import frc.robot.subsystems.ClimberArm;
import frc.robot.subsystems.ClimberPneumatics;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shifter;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.ClimberArm.Sides;
import frc.robot.utilities.Ports;
import frc.robot.commands.climb.ClimbSequence;
import frc.robot.commands.climb.ClimberArmMO;
import frc.robot.commands.climb.RaiseArm;
import frc.robot.commands.conveyor.ConveyorMO;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.drivetrain.EncoderDrive;
import frc.robot.commands.intake.IntakeArmDefault;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetLoad;
import frc.robot.commands.intake.SetUp;
import frc.robot.devices.LEDs;
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
    private ClimberArm leftArm, rightArm;
    //private Turret turret;
    private ClimberPneumatics climberPneumatics;

    //private Lemonlight limelight;
    // private ColorSensorV3 colorSensor;

    //private DoubleSolenoid buddySolenoid;
    //private Solenoid lock;

    private Command autoInit;
    private Command teleInit;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();
        logger = new SyncLogger();

        leds = new LEDs();
        LEDRange shifterRange = leds.getAllLedsRangeController();

        controller1 = new ControllerDriver(Ports.XBOX_PORT, logger);
        // launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT, logger);
        // joystick = new JoystickDriver(Ports.JOYSTICK_PORT, logger);

        compressor = new Compressor(Ports.PCM_1);
        compressor.setClosedLoopControl(true);

        // drivetrain = new Drivetrain();
        // shifter = new Shifter(shifterRange);
        // conveyor = new Conveyor();
        // intakeArm = new IntakeArm();
        shooter = new Shooter();
        // leftArm = new ClimberArm(Sides.LEFT);
        // rightArm = new ClimberArm(Sides.RIGHT);
        // turret = new Turret();
        // climberPneumatics = new ClimberPneumatics();

        // buddySolenoid = new DoubleSolenoid(Ports.PCM_1, Ports.OPEN_CLAMP,
        // Ports.CLOSE_CLAMP);

        // gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
        // limelight = new Lemonlight();
        // colorSensor = new ColorSensorV3(Port.kOnboard);

        setDefaultCommands();
        configureButtonBindings();

        logger.addElements(drivetrain, shifter);
        // scheduler.setDefaultCommand(logger, logger);
    }

    private void setDefaultCommands() {
        shooter.setDefaultCommand(new RunCommand(
            () -> shooter.setPower(controller1.rightTrigger.get()),
            shooter
        ));
    }

    private void configureButtonBindings() {
        
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

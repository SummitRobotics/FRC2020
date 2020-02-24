package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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
import frc.robot.commands.conveyor.ConveyorAutomation;
import frc.robot.commands.conveyor.ConveyorMO;
import frc.robot.commands.drivetrain.EncoderDrive;
import frc.robot.commands.intake.IntakeArmMO;
import frc.robot.commands.intake.SetDown;
import frc.robot.commands.intake.SetUp;
import frc.robot.devices.Lemonlight;
import frc.robot.devices.PigeonGyro;

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

    private Compressor compressor;

    private Drivetrain drivetrain;
    private Shifter shifter;
    private Conveyor conveyor;
    private IntakeArm intakeArm;
    private Shooter shooter;
    private ClimberArms climber;
    private Turret turret;
    private BuddyClimb buddyClimb;

    private PigeonGyro gyro;
    private Lemonlight limelight;

    private DoubleSolenoid buddySolenoid;
    private Solenoid lock;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();
        logger = new SyncLogger();

        controller1 = new ControllerDriver(Ports.XBOX_PORT, logger);
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT, logger);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT, logger);

        launchpad.buttonA.toggleBind();
        launchpad.buttonB.pressBind();

        compressor = new Compressor(Ports.PCM_1);
        compressor.setClosedLoopControl(true);

        //drivetrain = new Drivetrain();
        //shifter = new Shifter();
        //conveyor = new Conveyor();
        intakeArm = new IntakeArm();
        //shooter = new Shooter();
        //climber = new ClimberArms();
        //turret = new Turret();
        //buddyClimb = new BuddyClimb(climber);

        //buddySolenoid = new DoubleSolenoid(Ports.PCM_1, Ports.OPEN_CLAMP, Ports.CLOSE_CLAMP);
        //lock = new Solenoid(Ports.PCM_1, 5);

        //gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
        //limelight = new Lemonlight();

        setDefaultCommands();

        logger.addElements(drivetrain, gyro, shifter);
        //scheduler.setDefaultCommand(logger, logger);

        configureButtonBindings();
    }

    private void setDefaultCommands() {
        launchpad.buttonC.whenPressed(new RunCommand(
            () -> System.out.println(intakeArm.getUpperLimit())
        ));
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

        /*
        drivetrain.setDefaultCommand(new ArcadeDrive(
            drivetrain, 
            shifter, 
            controller1.rightTrigger, 
            controller1.leftTrigger, 
            controller1.leftX
        ));
        */

        //launchpad.buttonE.whileActiveOnce(new IntakeArmMO(intakeArm, controller1.leftY, controller1.rightBumper));
        //launchpad.buttonE.pressBind();
    }

    private void configureButtonBindings() {
        launchpad.buttonE.whileActiveContinuous(new IntakeArmMO(
            intakeArm, joystick.axisY, joystick.trigger));
        launchpad.buttonE.pressBind();

        launchpad.buttonC.whenPressed(new SetUp(intakeArm));
        launchpad.buttonB.whenPressed(new SetDown(intakeArm));

        launchpad.buttonC.pressBind();
        launchpad.buttonB.pressBind();

        /*
        launchpad.buttonE.toggleWhenPressed(new StartEndCommand(
            climber::extendClimb,
            climber::releaseClimb
        ));
        launchpad.buttonE.toggleBind();

        launchpad.buttonB.toggleWhenPressed(new StartEndCommand(
            () -> buddySolenoid.set(Value.kForward),
            () -> buddySolenoid.set(Value.kReverse)
        ));
        launchpad.buttonB.toggleBind();
        */

        /*
        launchpad.buttonC.toggleWhenPressed(new StartEndCommand(
            () -> lock.set(true),
            () -> lock.set(false)
        ));
        launchpad.buttonC.toggleBind();
        */

        /*
        launchpad.buttonD.toggleWhenPressed(new StartEndCommand(
            shifter::highGear,
            shifter::lowGear
        ));
        launchpad.buttonD.toggleBind();
        */

        /*
        launchpad.buttonE.whenPressed(new EncoderDrive(drivetrain, 500));
        launchpad.buttonE.whenReleased(new EncoderDrive(drivetrain, -500));
        */
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

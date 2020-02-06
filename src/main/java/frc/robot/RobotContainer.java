package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shifter;
import frc.robot.utilities.Ports;
import frc.robot.commands.*;
import frc.robot.commands.conveyor.ConveyorMO;
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

    private Drivetrain drivetrain;
    private Shifter shifter;
    private Conveyor conveyor;

    private PigeonGyro gyro;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();
        logger = new SyncLogger();

        controller1 = new ControllerDriver(Ports.XBOX_PORT, logger);
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT, logger);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT, logger);

        drivetrain = new Drivetrain();
        shifter = new Shifter();
        conveyor = new Conveyor();

        gyro = new PigeonGyro(Ports.PIGEON_IMU.port);

        scheduler.setDefaultCommand(drivetrain, new ArcadeDrive(drivetrain, shifter, controller1.rightTrigger, controller1.leftTrigger, controller1.leftX));

        logger.addElements(drivetrain, gyro, shifter);
        scheduler.setDefaultCommand(logger, logger);

        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by instantiating a {@link GenericHID} or one of its subclasses
     * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
     * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        launchpad.buttonG.whenHeld(new ConveyorMO(conveyor, joystick.axisY), false);
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

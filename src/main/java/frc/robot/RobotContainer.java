package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.ButtonDriver;
import frc.robot.drivetrain.Drivetrain;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private CommandScheduler scheduler;

  private SyncLogger logger;

  private ButtonDriver buttonDriver;

  private Drivetrain drivetrain;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    scheduler = CommandScheduler.getInstance();
    logger = new SyncLogger();

    buttonDriver = new ButtonDriver(logger);

    drivetrain = new Drivetrain();
    drivetrain.setDefaultCommand(new RunCommand(() -> drivetrain.arcadeDrive(
      buttonDriver.rightTrigger() - buttonDriver.leftTrigger(), 
      buttonDriver.leftStickX()), 
      drivetrain)
    );

    logger.addElements(drivetrain);

    scheduler.registerSubsystem(drivetrain, logger);

    configureButtonBindings();
  }

    /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
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

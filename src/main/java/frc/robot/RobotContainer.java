package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.ControllerDriver;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Pneumatics;
import frc.robot.utilities.Constants;
import frc.robot.commandgroups.AppeaseDuane;
import frc.robot.commands.*;
import frc.robot.devices.PigeonGyro;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private CommandScheduler scheduler;

  private SyncLogger logger;

  // public just to make things work
  private ControllerDriver controller1;

  // public just to make things work
  private PigeonGyro gyro;
  private Drivetrain drivetrain;

  private Pneumatics pneumatics;



  //commands

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    scheduler = CommandScheduler.getInstance();
    logger = new SyncLogger();

    controller1 = new ControllerDriver(logger, Constants.XBOX_PORT);

    gyro = new PigeonGyro(Constants.PIGEON_IMU);
    drivetrain = new Drivetrain();
    scheduler.setDefaultCommand(drivetrain, new ArcadeDrive(drivetrain, controller1));

    pneumatics = new Pneumatics();
    CommandScheduler.getInstance().schedule(new Shift(pneumatics, true));

    logger.addElements(drivetrain, gyro);
    scheduler.registerSubsystem(logger);

    configureButtonBindings();
  }

    /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    controller1.buttonA.whenPressed(new Shift(pneumatics));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new AppeaseDuane(drivetrain, gyro);
  }
}

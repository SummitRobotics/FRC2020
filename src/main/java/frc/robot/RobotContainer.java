package frc.robot;

import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.logging.SyncLogger;
import frc.robot.oi.ControllerDriver;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shifter;
import frc.robot.utilities.Ports;
import frc.robot.commandgroups.AppeaseDuane;
import frc.robot.commands.*;
import frc.robot.devices.LEDs;
import frc.robot.devices.PigeonGyro;
import frc.robot.devices.LEDs.LEDRange;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private CommandScheduler scheduler;

  private SyncLogger logger;

  private ControllerDriver controller1;

  // public just to make things work
  private PigeonGyro gyro;
  private Drivetrain drivetrain;
  private Shifter shifter;
  private LEDs leds;

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    scheduler = CommandScheduler.getInstance();
    logger = new SyncLogger();

    controller1 = new ControllerDriver(logger);

    gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
    drivetrain = new Drivetrain();
    shifter = new Shifter();
    leds = new LEDs();
    
    //scheduler.setDefaultCommand(drivetrain, new ArcadeDrive(drivetrain, controller1, shifter));

    logger.addElements(drivetrain, gyro, shifter);
    scheduler.setDefaultCommand(logger, logger);

    LEDRange range = leds.getRangeController(0, 28);

    leds.setDefaultCommand(new RunCommand(
      () -> range.setColor(new Color8Bit(
        (int) (controller1.leftTrigger() * 255), 
        (int) (controller1.rightTrigger() * 255), 
        (int) (controller1.leftStickX() *255)
    )), leds));

    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    controller1.leftBumper.whenPressed(new Shift(shifter));
    controller1.dPadRight.whenPressed(new GyroTurn(gyro, drivetrain, 90));
    controller1.dPadLeft.whenPressed(new GyroTurn(gyro, drivetrain, -90));
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

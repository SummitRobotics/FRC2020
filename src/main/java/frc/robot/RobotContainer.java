package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.logging.Logger;
import frc.robot.logging.SyncLogger;
import frc.robot.subsystems.Drivetrain;

public class RobotContainer {

  private CommandScheduler scheduler;

  private SyncLogger logger;

  private Drivetrain drivetrain;

  public RobotContainer() {
    scheduler = CommandScheduler.getInstance();

    drivetrain = new Drivetrain();

    scheduler.registerSubsystem(drivetrain);

    logger = new SyncLogger(new Logger[] {
      drivetrain
    });
    scheduler.registerSubsystem(logger);

    configureButtonBindings();
  }

  private void configureButtonBindings() {
  }


  public Command getAutonomousCommand() {
    return null;
  }
}

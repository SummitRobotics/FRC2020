package frc.robot;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;



/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    private CommandScheduler scheduler;


    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();
        

        setDefaultCommands();
        configureButtonBindings();

    }

    private void setDefaultCommands() {

    }

    private void configureButtonBindings() {
        
    }

    /**
     * runs when robot is inited to telyop
     */
    public void teleopInit() {

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

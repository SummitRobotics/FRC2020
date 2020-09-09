package frc.robot;

import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.oi.ControllerDriver;
import frc.robot.oi.JoystickDriver;
import frc.robot.oi.LaunchpadDriver;
import frc.robot.subsystems.*;
import frc.robot.utilities.Colors;
import frc.robot.utilities.Ports;
import frc.robot.commands.drivetrain.ArcadeDrive;
import frc.robot.commands.drivetrain.EncoderDrive;

import frc.robot.commands.pathfollowing.GenerateRecording;
//import frc.robot.commands.shooter.ShooterTester;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
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

    private ControllerDriver controller1;
    private LaunchpadDriver launchpad;
    private JoystickDriver joystick;

    private LEDs leds;
    private Compressor compressor;

    private PigeonGyro gyro;
    private Drivetrain drivetrain;

    // private Lemonlight limelight;
    private ColorSensorV3 colorSensor;
    private Lemonlight limelight;

    private Command autoInit;
    private Command teleInit;

    private LEDRange allLEDS;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();

        leds = new LEDs();
        int[] allRange = {0, 29};
        allLEDS = new LEDRange(leds, allRange);

        controller1 = new ControllerDriver(Ports.XBOX_PORT);
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT);
        joystick = new JoystickDriver(Ports.JOYSTICK_PORT);

        compressor = new Compressor(Ports.PCM_1);
        compressor.setClosedLoopControl(true);

        gyro = new PigeonGyro(0);
        drivetrain = new Drivetrain(gyro, () -> {return true;});
  

        // gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
        limelight = new Lemonlight();
        //colorSensor = new ColorSensorV3(Port.kOnboard);

        setDefaultCommands();
        configureButtonBindings();

        autoInit = new SequentialCommandGroup();

        // things that happen when the robot is inishlided
        teleInit = new SequentialCommandGroup(
                
                new InstantCommand(() -> {
                    launchpad.bigLEDRed.set(false);
                    launchpad.bigLEDGreen.set(true);
                })
               
                );
    }

    private void setDefaultCommands() {
        // drive by controler
        drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain, controller1.rightTrigger,
                controller1.leftTrigger, controller1.leftX));

    }

    private void configureButtonBindings() {
        // Launchpad bindings
        
    }

    /**
     * runs when robot is inited to telyop
     */
    public void teleopInit() {
        //inishlises robot
        scheduler.schedule(teleInit);

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new SequentialCommandGroup(
            autoInit,
            new EncoderDrive(drivetrain, 50, 50)
        );
    }
}

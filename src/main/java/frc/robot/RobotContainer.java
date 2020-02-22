package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
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
import frc.robot.commands.intake.IntakeArmMO;
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

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        scheduler = CommandScheduler.getInstance();
        logger = new SyncLogger();

        //controller1 = new ControllerDriver(Ports.XBOX_PORT, logger);
        launchpad = new LaunchpadDriver(Ports.LAUNCHPAD_PORT, logger);
        //joystick = new JoystickDriver(Ports.JOYSTICK_PORT, logger);

        launchpad.buttonA.toggleBind();
        
        //compressor = new Compressor(Ports.PCM_1);
        //compressor.setClosedLoopControl(true);

        //drivetrain = new Drivetrain();
        //shifter = new Shifter();
        //conveyor = new Conveyor();
        //intakeArm = new IntakeArm();
        //shooter = new Shooter();
        //climber = new ClimberArms();
        //turret = new Turret();
        //buddyClimb = new BuddyClimb(climber);

        //gyro = new PigeonGyro(Ports.PIGEON_IMU.port);
        //limelight = new Lemonlight();

        setDefaultCommands();

        logger.addElements(drivetrain, gyro, shifter);
        //scheduler.setDefaultCommand(logger, logger);

        configureButtonBindings();
    }

    private void setDefaultCommands() {
        //drivetrain.setDefaultCommand(new ArcadeDrive(drivetrain, shifter, controller1.rightTrigger, controller1.leftTrigger, controller1.leftX));
        //conveyor.setDefaultCommand(new ConveyorAutomation(conveyor, shooter, limelight));
        
        /*
        climber.setDefaultCommand(new RunCommand(
            () -> {
                double power = controller1.leftY.get();
                if (Math.abs(power) < 0.1) {
                    climber.setPower(0);
                } else {
                    climber.setPower(power);
                }
            }, climber));
        */
    }

    private void configureButtonBindings() {
        //launchpad.buttonG.whenHeld(new ConveyorMO(conveyor, joystick.axisY), false);
        //launchpad.buttonG.whenHeld(new IntakeArmMO(intakeArm, joystick.axisY, joystick.trigger), false);

        /*
        controller1.leftBumper.whenHeld(new StartEndCommand(
            shifter::lowGear,
            shifter::highGear,
            shifter
        ));
        */

        /*
        controller1.buttonA.toggleWhenPressed(new StartEndCommand(
            climber::extendClimb,
            climber::releaseClimb
        ));
        */

        //controller1.buttonA.whenHeld(new ConveyorMO(conveyor, controller1.rightY));
        //controller1.buttonB.whenHeld(new IntakeArmMO(intakeArm, controller1.rightY, controller1.rightBumper));
        /*
        controll  er1.buttonX.whenHeld(new RunCommand(
            () -> {
                double power = controller1.rightY.get();
                if (-0.05 < power && power < 0.05) {
                    turret.setPower(0);
                } else {
                    turret.setPower(power);
                }
                System.out.println(power);
            }, turret)); 
        */

        /*
        controller1.buttonX.whenHeld(new RunCommand(
            () -> {
                double power = controller1.rightY.get();

                if (-0.05 < power && power < 0.05) {
                    power = 0;
                }

                shooter.setPower(power);
                System.out.println(power);
            }
        ));
        */

        //controller1.buttonY.whenPressed(new InstantCommand(buddyClimb::clamp));


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

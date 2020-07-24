package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.utilities.Colors;

public class SetDown extends CommandBase {

    private final static double targetTime = 0.5;
    protected IntakeArm intake;

    protected Timer timer = new Timer();
    protected boolean end;

    protected double startTime;

    private LEDRange leds;
    public int LEDpriority = 2;

    public SetDown(IntakeArm intake, LEDRange leds) {
        this.leds = leds;
        this.intake = intake;

        end = false;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        leds.addLEDCall("ArmDown", new LEDCall(LEDpriority, LEDCall.ffh(Colors.Blue, Colors.Off)));
        
        timer.reset();
        timer.start();

        intake.openLock();

        if (intake.getState() == States.UP) {
            intake.setPivotPower(0.2);

        } else {
            end = true;
        }

        intake.setIntakePower(IntakeArm.intakePower);
        intake.setState(States.DOWN_YES_INTAKE);
    }

    @Override
    public void end(boolean interrupted) {
        leds.removeLEDCall("ArmDown");

        intake.setPivotPower(0);
        
        timer.stop();
        timer.reset();

        end = false;
    }

    @Override
    public boolean isFinished() {
        return end || timer.get() > targetTime;
    }
}

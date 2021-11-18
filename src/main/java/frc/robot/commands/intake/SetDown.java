package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commandegment.CommandBase;
import frc.robot.devices.LEDs.LEDCall;
import frc.robot.devices.LEDs.LEDRange;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.LEDPriorities;

public class SetDown extends CommandBase {

    private final static double targetTime = 0.75;

    protected IntakeArm intake;

    protected Timer timer = new Timer();
    protected boolean end = false;

    protected double startTime;

    public SetDown(IntakeArm intake) {
        this.intake = intake;

        addRequirements(intake);
    }

    public SetDown getDuplicate(){
        return new SetDown(intake);
    }

    @Override
    public void initialize() {
        // new LEDCall("ArmDown", LEDPriorities.intakeDown, LEDRange.All).ffh(Colors.Blue, Colors.Off).activate();
        System.out.println("setdown start");
        LEDs.getInstance().addCall("ArmDown", new LEDCall(LEDPriorities.intakeDown, LEDRange.All).ffh(Colors.Blue, Colors.Off));
        
        timer.reset();
        timer.start();

        intake.openLock();

        if (intake.getState() == States.UP) {
            intake.setPivotPower(0.4);

        } else {
            end = true;
        }

        intake.setIntakePower(IntakeArm.intakePower);        
    }

    @Override
    public void end(boolean interrupted) {
        if(!interrupted){
            intake.setState(States.DOWN_YES_INTAKE);
        }
        System.out.println("setdown ended");
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

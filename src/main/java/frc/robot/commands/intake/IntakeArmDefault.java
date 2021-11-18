package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commandegment.CommandBase;
import frc.robot.oi.drivers.ShufhellboardDriver;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.utilities.Functions;
import frc.robot.utilities.RollingAverage;
import frc.robot.utilities.lists.Colors;
import frc.robot.utilities.lists.StatusPrioritys;

public class IntakeArmDefault extends CommandBase {

    private IntakeArm intake;
    private Timer fakePIDTimer;

    private boolean resetToggle;
    private boolean resetAvrage;

    private RollingAverage averageCurrent;
    private Boolean isStuck;
    private Timer reverseTimer;

    private final double StuckCurrent = 10;
    private final double reverseTime = 0.8;

    private final static double
    POWER_RAMP_RATE = 1,
    MAX_CORRECTION_POWER = 0.3;

    public IntakeArmDefault(IntakeArm intake) {
        this.intake = intake;

        fakePIDTimer = new Timer();
        reverseTimer = new Timer();
        resetToggle = true;
        resetAvrage = true;
        isStuck = false;
        averageCurrent = new RollingAverage(20, false);

        addRequirements(intake);
    }
    
    @Override
    public void execute() {
		//puts up arm if limit switch says it is not up and it should be up
		if (intake.state == States.UP) {

            //resets things for stuck reversing
            resetAvrage = true;
            isStuck = false;
            reverseTimer.stop();
            reverseTimer.reset();

			if (!intake.getUpperLimit()) {
                if (resetToggle) {
                    fakePIDTimer.start();
                    resetToggle = false;
                }
                
                double power = -Functions.clampDouble(
                    fakePIDTimer.get() * MAX_CORRECTION_POWER / POWER_RAMP_RATE, 
                    MAX_CORRECTION_POWER, 
                    -MAX_CORRECTION_POWER
                );

				//fake pid™
				//intake.setPivotPower(power);

			} else {
                if (!resetToggle) {
                    resetToggle = true;
                }

				fakePIDTimer.stop();
                fakePIDTimer.reset();
                
				intake.stop();
			}
        }
        //deals with stuck balls when intake down
        else if(intake.state == States.DOWN_YES_INTAKE){
            //resets avrage is was previosuly in the up pos
            if(resetAvrage){
                averageCurrent.reset();
                resetAvrage = false;
            }

            //gets and avrages motor current
            double current = intake.getIntakeCurrent();
            //System.out.println(current);
            averageCurrent.update(current);

            //runs motor backward if current is to high or ball is stuck
            if((averageCurrent.getAverage() > StuckCurrent) || isStuck){
                //if was not reported as stuck before (trigered by over current), do things to un-stick it
                if(!isStuck){
                    isStuck = true;
                    reverseTimer.start();
                    ShufhellboardDriver.statusDisplay.addStatus("unstuck", "unstucking ball", Colors.Yellow, StatusPrioritys.stuckBall);
                    intake.setIntakePower(-IntakeArm.intakePower);
                }
                //if time reversing expired reset to deafult
                else if(reverseTimer.get() > reverseTime){
                    isStuck = false;
                    reverseTimer.stop();
                    reverseTimer.reset();
                    ShufhellboardDriver.statusDisplay.removeStatus("unstuck");
                    intake.setIntakePower(IntakeArm.intakePower);
                }
                //if already running do nothing beacuse vals are sticky
            }
               
        }
        //do nothing if intake is in another state (load)
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.commandegment.CommandBase;
import frc.robot.commandegment.CommandSchedulest;
import frc.robot.commandegment.InstantCommand;
import frc.robot.commandegment.SequentialCommandGroup;
import frc.robot.commandegment.WaitCommand;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.devices.LEDs.LEDs;
import frc.robot.subsystems.IntakeArm;

public class SetUp extends SequentialCommandGroup {

    private IntakeArm intake;
    private boolean end;

    private int loopsClosed = 0;

    private double intakeLiftPower = -0.55;

    public SetUp(IntakeArm intake) {
        this.intake = intake;
        end = false;

        addCommands(
            new SetUpProxy(),
            new WaitCommand(0.25),
            new InstantCommand(() -> {
                intake.setPivotPower(0);
                intake.closeLock();
            }, intake)
        );
        loopsClosed = 0;
        setPriority(2);
    }

    private class SetUpProxy extends CommandBase {
        private Timer timer = new Timer();
        private double intakeLPower = intakeLiftPower;
        private double timeBeforePowerIncrese = 3;

        @Override
        public void initialize() {
            CommandSchedulest.getInstance().cancleCommandsForSubsystem(intake, getScedualedPriority(), this);
            System.out.println("setup start");
            timer.reset();
            timer.start();

            intake.openLock();
    
            if (intake.getState() == States.UP) {
                end = true;
                
            } else {
    
                if (intake.getState() == States.DOWN_YES_INTAKE) {
                    intake.setIntakePower(IntakeArm.intakePower);
                }
    
                intake.setPivotPower(intakeLPower);
            }
            loopsClosed = 0;
        }

        // //fix for trying to rase intake if battery is low
        // //this will gradualy increse the motor power if the intake has not raised after 3s
        // //this way the intake will not be left down if the battery is low and the motor therefor has less power
        // @Override
        // public void execute() {
        //     System.out.println("setup exicuted");
        //     if((intakeLPower < 1) && (timer.get() > timeBeforePowerIncrese)){
        //         intakeLPower = intakeLPower + Math.copySign(0.05, intakeLPower);
        //         intake.setPivotPower(intakeLPower);
        //     } 
        // }
    
        @Override
        public void end(boolean interrupted) {   
            System.out.println("setup end");
            if(!interrupted){
                intake.setState(States.UP);
            }
            LEDs.getInstance().removeCall("ArmDown"); 
            intake.setIntakePower(0);
            intake.setPivotPower(0);
            timer.stop();
            timer.reset();
            end = false;
        }
    
        @Override
        public boolean isFinished() {
            boolean sw = intake.getUpperLimit();
            if(sw){
                intake.closeLock();
                loopsClosed++;
            }
            return (sw && loopsClosed > 3) || end;
        }    
    }

}

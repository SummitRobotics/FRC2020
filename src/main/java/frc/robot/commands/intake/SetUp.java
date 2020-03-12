package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.IntakeArm.States;
import frc.robot.subsystems.IntakeArm;

public class SetUp extends SequentialCommandGroup {

    IntakeArm intake;
    boolean end;

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
    }

    private class SetUpProxy extends CommandBase {

        @Override
        public void initialize() {
            intake.openLock();
    
            if (intake.getState() == States.UP) {
                end = true;
                
            } else {
    
                if (intake.getState() == States.DOWN_YES_INTAKE) {
                    intake.setIntakePower(IntakeArm.intakePower);
                }
    
                intake.setState(States.UP);
    
                intake.setPivotPower(-0.38);
            }
        }
    
        @Override
        public void end(boolean interrupted) {    
            intake.setIntakePower(0);
    
            end = false;
        }
    
        @Override
        public boolean isFinished() {
            return intake.getUpperLimit() || end;
        }    
    }

}

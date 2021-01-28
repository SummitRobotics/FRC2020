

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.lists.Ports;

public class ClimberPneumatics extends SubsystemBase {

    private Solenoid climb;

    private boolean climbState;

    public ClimberPneumatics() {
        climb = new Solenoid(Ports.PCM_1, Ports.EXTEND_CLIMB);

        climbState = true;
    }

    public void extendClimb() {
        climbState = true;
        climb.set(true);
    }

    public void retractClimb() {
        climbState = false;
        climb.set(false);
    }
    
    public void toggleClimb() {
        climbState = !climbState;
        climb.set(climbState);
    }

    public boolean getClimbState() {
        return this.climbState;
    }
}

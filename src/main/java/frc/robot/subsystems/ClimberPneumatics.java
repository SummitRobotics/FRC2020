

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.Ports;

public class ClimberPneumatics extends SubsystemBase {

    private Solenoid climb;
    private DoubleSolenoid buddyClimb;

    public ClimberPneumatics() {
        climb = new Solenoid(Ports.PCM_1, Ports.EXTEND_CLIMB);
        buddyClimb = new DoubleSolenoid(Ports.PCM_1, Ports.OPEN_BUDDY_CLIMB, Ports.CLOSE_BUDDY_CLIMB);
    }

    public void extendClimb() {
        climb.set(true);
    }

    public void retractClimb() {
        climb.set(false);
    }

    public void extendBuddyClimb() {
        buddyClimb.set(Value.kForward);
    }

    public void retractBuddyClimb() {
        buddyClimb.set(Value.kReverse);
    }
}

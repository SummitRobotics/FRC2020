/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.ClimberArms.ClimbDefaults;
import frc.robot.utilities.Ports;

public class BuddyClimb extends SubsystemBase {

    private ClimberArms climber;
    private DoubleSolenoid clamp;

    private boolean clamped;

    public BuddyClimb(ClimberArms climber) {
        this.climber = climber;

        clamp = new DoubleSolenoid(Ports.PCM_1, Ports.OPEN_CLAMP, Ports.CLOSE_CLAMP);
        clamped = false;

        clamp.set(Value.kReverse);
    }

    public void clamp() {
        if (!clamped /* && climber.state == ClimbDefaults.RAISED*/) {
            clamp.set(Value.kReverse);
        }
    }
}

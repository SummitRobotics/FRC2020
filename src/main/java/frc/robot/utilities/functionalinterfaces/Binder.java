package frc.robot.utilities.functionalinterfaces;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface Binder {
	public Trigger bind(Trigger t, Command c, boolean i);
}
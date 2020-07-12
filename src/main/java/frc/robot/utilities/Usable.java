package frc.robot.utilities;

public interface Usable {
	public void using(Object user);
	public void release(Object user);
	public boolean inUse();
}
package frc.robot.utilities;

/**
 * interface for makign a button or axis usable for moes
 */
public interface Usable {
	public void using(Object user);
	public void release(Object user);
	public boolean inUse();
}
package frc.robot.utilities.powerlimiting;

public interface LimitedSubsystem {

    public int[] usedPorts();
    public double getPriority();
    public void limitPower(double factor);
}
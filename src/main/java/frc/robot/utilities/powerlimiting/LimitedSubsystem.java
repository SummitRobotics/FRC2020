package frc.robot.utilities.powerlimiting;

public interface LimitedSubsystem {

    public double getPriority();
    public void limitPower(double amount);
}
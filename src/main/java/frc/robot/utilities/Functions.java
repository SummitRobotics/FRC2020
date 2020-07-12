package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utilities.functionalinterfaces.Binder;

/**
 * Contains various static utility functions for use throughout the program
 */
public class Functions {

    /**
     * Clamps a double between two values
     * @param in the input value to clamp
     * @param max the maximum you want it to be
     * @param min the minimum for it to be
     * @return the clamped double
     */    
    public static double clampDouble(double in, double max, double min) {
        if (in > max) {
            return max;
        }
        else if (in < min) {
            return min;
        }
        else {
            return in;
        }
    }

    public static double deadzone(double value, double range) {
        value = value - (Math.copySign(range, value));
        return value;
    }

    /**
     * Tells if value is within a target range
     * @param toCompare the value to compare
     * @param target the target value
     * @param error the valid range around the target
     * @return if the value is within the range
     */
    public static boolean isWithin(double toCompare, double target, double error){
        return Math.abs(toCompare - target) <= (error / 2);
    }
}

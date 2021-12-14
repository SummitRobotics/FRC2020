package frc.robot.oi.inputs;

import java.util.HashMap;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commandegment.CommandBase;
import frc.robot.utilities.Functions;
import frc.robot.utilities.PrioritisedInput;

/**
 * Wrapper for axes that allows for better management
 */
public class OIAxis implements PrioritisedInput {

	private final static double DEFAULT_DEADZONE = 0.05;

	protected DoubleSupplier getter;
	protected double deadzone;
	private HashMap<Object, Integer> users;



	public OIAxis(DoubleSupplier getter) {
		this(getter, DEFAULT_DEADZONE);
	}

	public OIAxis(DoubleSupplier getter, double deadzone) {
		this.getter = getter;
		this.deadzone = deadzone;

	}

	/**
	 * Gets the position of the axis
	 * 
	 * @return the position
	 */
	public double get() {
		double position = getter.getAsDouble();

		if (Functions.isWithin(position, 0, deadzone)) {
			return 0;
		}

		return (1 + deadzone) * position - Math.copySign(deadzone, position);
	}

	/**
	 * @return the deadzone
	 */
	public double getDeadzone() {
		return deadzone;
	}

	/**
	 * @param deadzone the deadzone to set
	 */
	public void setDeadzone(double deadzone) {
		this.deadzone = deadzone;
	}

	public double getWithPriority(Object user){
		return getWithPriorityOrDeafult(user, 0);
	}

	public double getWithPriorityOrDeafult(Object user, double deafult){
        if(ableToUse(user)){
            return get();
        }
        else{
            return deafult;
        }
    }

public void using (CommandBase user){
	register(user, user.getScedualedPriority());
}

    @Override
    public void register(Object user, int priority) {
        users.putIfAbsent(user, priority);
        
    }

    @Override
    public void release(Object user) {
        users.remove(user);
        
    }

    @Override
    public boolean ableToUse(Object user) {
        //if the object trying to use is not registered say no
        if(users.containsKey(user)){
            //gets the priority atempting to be used
            int atemptPriority = users.get(user);
            //loops through all the objects and sees if there is one with a larger priority registered
            for (Object x : users.keySet()){
                if(atemptPriority < users.get(x)){
                    return false;
                }
            }
            //return true if there is no bigger priority
            return true;
        }
        else{
            return false;
        }
    }

}
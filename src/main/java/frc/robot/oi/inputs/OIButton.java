package frc.robot.oi.inputs;

import java.util.HashMap;
import java.util.function.BooleanSupplier;

import frc.robot.commandegment.CommandBase;
import frc.robot.commandegment.button.Button;
import frc.robot.oi.Konami;
import frc.robot.utilities.PrioritisedInput;

/**
 * Wrapper class for WPI's button that allows for better management
 */
public class OIButton extends Button implements PrioritisedInput {

    private HashMap<Object, Integer> users;

    public OIButton(BooleanSupplier getter) {
        super(getter);

        whenHeld(Konami.nonRegisteredButtonPress());

        users = new HashMap<>();
    }

    public OIButton(BooleanSupplier getter, String id) {
        super(getter);

        whenHeld(Konami.registeredButtonPress(id));

        users = new HashMap<>();
    }

    public OIButton() {
        super();

        users = new HashMap<>();
    }

    /**
	 * gets the axis useing the commands priority
	 * @param user the object atempting to get the axis value (same one that registered with a priority)
	 * @return the value if this object has the highest priority or false
	 */
    public boolean getWithPriority(Object user){
		return getWithPriorityOrDeafult(user, false);
	}

    /**
	 * gets the axis useing the commands priority
	 * @param user the object atempting to get the axis value (same one that registered with a priority)
	 * @param deafult the value that should be returned if the axis is used bu somthign else
	 * @return the value if avalible or the deafult if not
	 */
    public boolean getWithPriorityOrDeafult(Object user, boolean deafult){
        if(ableToUse(user)){
            return get();
        }
        else{
            return deafult;
        }
    }

    public void using(CommandBase user) {
        users.putIfAbsent(user, user.getScedualedPriority());
        
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

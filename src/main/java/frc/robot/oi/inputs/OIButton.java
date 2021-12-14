package frc.robot.oi.inputs;

import java.util.HashMap;
import java.util.function.BooleanSupplier;

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

    public boolean getWithPriorityOrDeafult(Object user, boolean deafult){
        if(ableToUse(user)){
            return get();
        }
        else{
            return deafult;
        }
    }

    @Override
    public void using(Object user, int priority) {
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

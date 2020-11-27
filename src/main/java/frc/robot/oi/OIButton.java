package frc.robot.oi;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.utilities.Usable;
import frc.robot.utilities.functionalinterfaces.ButtonGetter;

/**
 * Wrapper class for WPI's button that allows for better management
 */
public class OIButton extends Button implements Usable {

    private ButtonGetter getter;

    private ArrayList<Object> users;

    public OIButton(ButtonGetter getter) {
        super();

        this.getter = getter;

        users = new ArrayList<>();
    }

    /**
     * Gets the button value
     * @return the button value
     */
    @Override
    public boolean get() {
        return getter.get();
    }

    @Override
    public void using(Object user) {
        users.add(user);
    }

    @Override
    public void release(Object user) {
        users.remove(user);
    }

    @Override
    public boolean inUse() {
        return !users.isEmpty();
    }
}

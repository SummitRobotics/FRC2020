package frc.robot.oi;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.utilities.Usable;

/**
 * Wrapper class for WPI's button that allows for better management
 */
public class OIButton extends Button implements Usable {

    private BooleanSupplier getter;

    private ArrayList<Object> users;

    public OIButton(BooleanSupplier getter) {
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
        return getter.getAsBoolean();
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

package frc.robot.oi;

import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Usable;
import frc.robot.utilities.functionalinterfaces.ButtonGetter;

/**
 * Wrapper class for WPI's button that adds logging functionality
 */
public class LoggerButton extends Button implements Logger, Usable {

    private LoggerRelations logReference;
    private ButtonGetter getter;

    private ArrayList<Object> users;

    public LoggerButton(ButtonGetter getter, LoggerRelations logReference) {
        super();

        this.logReference = logReference;
        this.getter = getter;

        users = new ArrayList<>();
    }

    public LoggerButton(ButtonGetter getter, LoggerRelations logReference, SyncLogger logger) {
        this(getter, logReference);

        logger.addElements(this);
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
    public double[] getValues(double[] values) {
        values[logReference.value] = super.get() ? 1 : 0;
        return values;
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

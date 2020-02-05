package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;

interface ButtonGetter {
    boolean get();
}

public class LoggerButton extends Button implements Logger {

    private LoggerRelations logReference;
    private ButtonGetter getter;

    public LoggerButton(ButtonGetter getter, LoggerRelations logReference) {
        super();

        this.logReference = logReference;
        this.getter = getter;
    }

    public LoggerButton(ButtonGetter getter, LoggerRelations logReference, SyncLogger logger) {
        super();

        this.logReference = logReference;
        this.getter = getter;

        logger.addElements(this);
    }

    public LoggerButton assignToLogger(SyncLogger logger) {
        logger.addElements(this);
        return this;
    }

    @Override
    public boolean get() {
        return getter.get();
    }

    @Override
    public double[] getValues(double[] values) {
        values[logReference.value] = super.get() ? 1 : 0;
        return values;
    }    
}

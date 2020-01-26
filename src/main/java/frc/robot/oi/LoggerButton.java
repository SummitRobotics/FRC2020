package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;

interface Getter {
    boolean get();
}

public class LoggerButton extends Button implements Logger {

    private LoggerRelations logReference;
    private Getter getter;

    public LoggerButton(Getter getter, LoggerRelations logReference) {
        super();

        this.logReference = logReference;
        this.getter = getter;
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

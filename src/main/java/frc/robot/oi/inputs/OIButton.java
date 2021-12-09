package frc.robot.oi.inputs;

import java.util.function.BooleanSupplier;

import frc.robot.commandegment.button.Button;
import frc.robot.oi.Konami;

/**
 * Wrapper class for WPI's button that allows for better management
 */
public class OIButton extends Button {


    public OIButton(BooleanSupplier getter) {
        super(getter);

        whenHeld(Konami.nonRegisteredButtonPress());
    }

    public OIButton(BooleanSupplier getter, String id) {
        super(getter);

        whenHeld(Konami.registeredButtonPress(id));
    }

    public OIButton() {
        super();

    }

}

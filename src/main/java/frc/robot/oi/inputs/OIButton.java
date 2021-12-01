package frc.robot.oi.inputs;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

import frc.robot.commandegment.button.Button;
import frc.robot.oi.Konami;

/**
 * Wrapper class for WPI's button that allows for better management
 */
public class OIButton extends Button {

    private ArrayList<Object> users;

    public OIButton(BooleanSupplier getter) {
        super(getter);

        users = new ArrayList<>();

        whenHeld(Konami.nonRegisteredButtonPress());
    }

    public OIButton(BooleanSupplier getter, String id) {
        super(getter);

        users = new ArrayList<>();

        whenHeld(Konami.registeredButtonPress(id));
    }

    public OIButton() {
        super();

        users = new ArrayList<>();
    }

}

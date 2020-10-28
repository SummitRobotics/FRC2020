/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

import java.util.function.BooleanSupplier;

/**
 * Add your docs here.
 */
public class SimpleButton {

    private BooleanSupplier supplier;
    private boolean last;

    public SimpleButton(BooleanSupplier supplier) {
        this.supplier = supplier;

        last = false;
    }

    /**
     * Returns true only on the rising edge of a button press
     */
    public boolean get(){
        boolean current = supplier.getAsBoolean();
        boolean output = !last && current;

        last = current;

        return output;
    }
}

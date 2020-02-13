package frc.robot.devices;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;

public class Lemonlight implements Logger{

    NetworkTable limelight;

    NetworkTableEntry tv, tx, ty, ta, ledMode, camMode, pipeline;

    public Lemonlight() {
        limelight = NetworkTableInstance.getDefault().getTable("limelight");

        tv = limelight.getEntry("tv");
        tx = limelight.getEntry("tx");
        ty = limelight.getEntry("ty");
        ta = limelight.getEntry("ta");

        ledMode = limelight.getEntry("ledMode");
        camMode = limelight.getEntry("camMode");

        pipeline = limelight.getEntry("pipeline");
    }

    //logging
    @Override
    public double[] getValues(double[] values) {
        values[LoggerRelations.LEMONLIGHT_HAS_TARGET.value] = (hasTarget()) ? 1 : 0;
        values[LoggerRelations.LEMONLIGHT_X_OFF.value] = getHorizontalOffset();
        values[LoggerRelations.LEMONLIGHT_Y_OFF.value] = getVerticalOffset();     
        return values;
    }

    /**
     * Enum to describe the state of the LED
     */
    public enum LEDModes {
        PIPELINE(0),
        FORCE_OFF(1),
        FORCE_BLINK(2),
        FORCE_ON(3);

        public int value;
        private LEDModes(int value) {
            this.value = value;
        }
    }

    public enum CamModes {
        VISION_PROCESSOR(0),
        DRIVER_CAMERA(1);

        public int value;
        private CamModes(int value) {
            this.value = value;
        }
    }

    public void setLEDMode(LEDModes mode) {
        ledMode.setDouble(mode.value);
    }

    public void setCamMode(CamModes mode) {
        camMode.setDouble(mode.value);
    }

    public void setPipeline(int pipe) {
        pipeline.setDouble(pipe);
    }

    public boolean hasTarget() {
        return tv.getDouble(0) == 1;
    }

    public double getHorizontalOffset() {
        return tx.getDouble(0);
    }

    public double getVerticalOffset() {
        return ty.getDouble(0);
    }

    public double getAreaPercentage() {
        return ta.getDouble(0);
    }
}
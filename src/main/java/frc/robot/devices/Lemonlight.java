package frc.robot.devices;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Lemonlight {

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
        return tv.getBoolean(false);
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
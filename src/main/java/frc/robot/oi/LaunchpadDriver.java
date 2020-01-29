package frc.robot.oi;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;

public class LaunchpadDriver implements Logger {

    private DriverStation driverStation;
    private int port;

    public LoggerButton
    buttonA,
    buttonB,
    buttonC,
    buttonD,
    buttonE,
    buttonF,
    buttonG,
    buttonH,
    buttonI,
    buttonJ,
    buttonK;


    public LaunchpadDriver(int port, SyncLogger logger) {
        driverStation = DriverStation.getInstance();
        this.port = port;

        buttonA = new LoggerButton(() -> getRawButton(1), LoggerRelations.PLACEHOLDER);
        buttonB = new LoggerButton(() -> getRawButton(2), LoggerRelations.PLACEHOLDER);
        buttonC = new LoggerButton(() -> getRawButton(3), LoggerRelations.PLACEHOLDER);
        buttonD = new LoggerButton(() -> getRawButton(4), LoggerRelations.PLACEHOLDER);
        buttonE = new LoggerButton(() -> getRawButton(5), LoggerRelations.PLACEHOLDER);
        buttonF = new LoggerButton(() -> getRawButton(6), LoggerRelations.PLACEHOLDER);
        buttonG = new LoggerButton(() -> getRawButton(7), LoggerRelations.PLACEHOLDER);
        buttonH = new LoggerButton(() -> getRawButton(8), LoggerRelations.PLACEHOLDER);
        buttonI = new LoggerButton(() -> getRawButton(9), LoggerRelations.PLACEHOLDER);
        buttonJ = new LoggerButton(() -> getRawButton(10), LoggerRelations.PLACEHOLDER);
        buttonK = new LoggerButton(() -> getRawButton(11), LoggerRelations.PLACEHOLDER);

        logger.addElements(
            this,
            buttonA,
            buttonB,
            buttonC,
            buttonD,
            buttonE,
            buttonF,
            buttonG,
            buttonH,
            buttonI,
            buttonJ,
            buttonK  
        );
    }

    private double getRawAxis(int axis) {
        return driverStation.getStickAxis(port, axis);
    }

    private boolean getRawButton(int button) {
        return driverStation.getStickButton(port, button);
    }

    public double getAxisA() {
        return getRawAxis(1);
    }

    public double getAxisB() {
        return getRawAxis(2);
    }

    public double getAxisC() {
        return getRawAxis(3);
    }

    public double getAxisD() {
        return getRawAxis(4);
    }

    public double getAxisE() {
        return getRawAxis(5);
    }

    public double getAxisF() {
        return getRawAxis(6);
    }

    public double getAxisG() {
        return getRawAxis(7);
    }

    public double getAxisH() {
        return getRawAxis(8);
    }

    @Override
    public double[] getValues(double[] values) {
        values[LoggerRelations.PLACEHOLDER.value] = getAxisA();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisB();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisC();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisD();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisE();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisF();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisG();
        values[LoggerRelations.PLACEHOLDER.value] = getAxisH();
        return values;
    }

}
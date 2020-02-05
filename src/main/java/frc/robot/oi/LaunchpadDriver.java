package frc.robot.oi;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.logging.Logger;
import frc.robot.logging.LoggerRelations;
import frc.robot.logging.SyncLogger;
import frc.robot.utilities.Ports;
import edu.wpi.first.hal.HAL;

/**
 * Wrappper class for the TI Launchpad in mode 1
 */
public class LaunchpadDriver extends GenericDriver implements Logger {

    private int port;
    private int outputs;

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


    public LaunchpadDriver(SyncLogger logger) {
        this.port = Ports.LAUNCHPAD_PORT.port;

        //TODO - create actual logger values
        buttonA = new LoggerButton(getButtonGetter(1), LoggerRelations.PLACEHOLDER, logger);
        buttonB = new LoggerButton(getButtonGetter(2), LoggerRelations.PLACEHOLDER, logger);
        buttonC = new LoggerButton(getButtonGetter(3), LoggerRelations.PLACEHOLDER, logger);
        buttonD = new LoggerButton(getButtonGetter(4), LoggerRelations.PLACEHOLDER, logger);
        buttonE = new LoggerButton(getButtonGetter(5), LoggerRelations.PLACEHOLDER, logger);
        buttonF = new LoggerButton(getButtonGetter(6), LoggerRelations.PLACEHOLDER, logger);
        buttonG = new LoggerButton(getButtonGetter(7), LoggerRelations.PLACEHOLDER, logger);
        buttonH = new LoggerButton(getButtonGetter(8), LoggerRelations.PLACEHOLDER, logger);
        buttonI = new LoggerButton(getButtonGetter(9), LoggerRelations.PLACEHOLDER, logger);
        buttonJ = new LoggerButton(getButtonGetter(10), LoggerRelations.PLACEHOLDER, logger);
        buttonK = new LoggerButton(getButtonGetter(11), LoggerRelations.PLACEHOLDER, logger);

        logger.addElements(this);
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

    //outputs
    public void setOutput1(boolean state){
        setOutput(1, state);
    }

    public void setOutput2(boolean state){
        setOutput(2, state);
    }

    public void setOutput3(boolean state){
        setOutput(3, state);
    }

    public void setOutput4(boolean state){
        setOutput(4, state);
    }

    public void setOutput5(boolean state){
        setOutput(5, state);
    }

    public void setOutput6(boolean state){
        setOutput(6, state);
    }

    public void setOutput7(boolean state){
        setOutput(7, state);
    }

    public void setOutput8(boolean state){
        setOutput(8, state);
    }

    public void setOutput9(boolean state){
        setOutput(9, state);
    }

    public void setOutput10(boolean state){
        setOutput(10, state);
    }

    public void setOutput11(boolean state){
        setOutput(11, state);
    }

    /**
     * Black box to set outputs
     * @param outputNumber the output number
     * @param value the state of the output
     */
    public void setOutput(int outputNumber, boolean value) {
        outputs = (outputs & ~(1 << (outputNumber - 1))) | ((value ? 1 : 0) << (outputNumber - 1));
        HAL.setJoystickOutputs((byte) port, outputs, (short)0, (short)0);
    }

    //TODO - make actual logger values
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
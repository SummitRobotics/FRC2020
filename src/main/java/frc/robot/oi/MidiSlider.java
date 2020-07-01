package frc.robot.oi;

import frc.robot.utilities.Functions;

/**
 * Wrapper for axes that allows them to be both logged and passed as variables
 */
public class MidiSlider {

	private int getId;
	private int setId;
    private Midi midi;

	public MidiSlider(Midi midi, int getCCId, int setCCId) {
		this.midi = midi;
        setId = setCCId;
        getId = getCCId;
	}

	/**
	 * Gets the value of the slider
	 * @return the value of the slider (-1 - 1), deafults to 0
     * 
	 */
	public double get() {
        int num = (int)midi.getCC(getId, 63);
        if(num == 63){
            return 0;
        }
        else{
            return ((num)/63.5)-1;
        }
    }

    /**
     * sets the slider to a posision
     * @param value the posison you want it to go to (-1 - 1)
     * note that the value you put in may not always coraspond exactly to the
     * sliders reported posision, but it will be within ~.03 of the target
     * this is mostly intended to return the slider to 0, 1, or -1 as those
     * values will be exact
     */
    public void set(double value){
        value = Functions.clampDouble(value, 1, -1);
        int num = 0;
        if (value == 0){
            num = 63;
        }
        else{
            //turns -1 - 1 into 0-127 for slider
            num = (int)(((value+1)*63.5)+.5);
        }
        midi.sendCC(setId, num);
    }

    /**
     * gets the raw value of the slider
     * @return the raw value (0-127), deafults to 63
     */
    public int getRaw(){
        return (int)midi.getCC(getId, 63);
    }

    /**
     * sets the slider to a raw posision
     * @param setpoint the setpoint for the slider (0-127)
     */
    public void setRaw(int setpoint){
        midi.sendCC(setId, setpoint);
    }

}

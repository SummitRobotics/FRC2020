package frc.robot.oi;


/**
 * Wrapper for axes that allows them to be both logged and passed as variables
 */
public class MidiEncoder {

	private int getId;
	private int setId;
    private Midi midi;

	public MidiEncoder (Midi midi, int getCCId, int setCCId) {
		this.midi = midi;
        setId = setCCId;
        getId = getCCId;
	}

	/**
	 * Gets the value of the encoder
	 * 
	 * @return the value of the encoder (0-1), deafults to 0
     * note that the encoder being in the midle does not exactly coraspond with .5
     * but both ends of the encoder do coraspond with 0 and 1 exactly
	 */
	public double get() {
		return (midi.getCC(getId, 0)/127);
    }
    
    /**
     * gets the raw value of the encoder
     * @return the raw value (0-127), deafults to 0
     */
    public int getRaw(){
        return midi.getCC(getId, 0);
    }

    /**
     * sets the led ring mode of the encoder
     * @param ringmode 0=single, 1=pan, 2=fan, 3=spred, 4=trim
     */
    public void setRingMode(int ringmode){
        midi.sendCC(setId, ringmode);
    }

}

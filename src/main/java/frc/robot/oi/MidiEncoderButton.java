package frc.robot.oi;

import edu.wpi.first.wpilibj2.command.button.Button;


public class MidiEncoderButton extends Button{

	private int getId;
    private Midi midi;

	public MidiEncoderButton (Midi midi, int getCCId) {
		this.midi = midi;
        getId = getCCId;
    }
    
    /**
     * gets the state of the button, deafults to off
     */
    @Override
    public boolean get() {
        return midi.getNote(getId);
    }

}

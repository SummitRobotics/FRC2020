package frc.robot.oi;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.Trigger;


public class MidiLedButton extends Button{

	private int getId;
	private int setId;
    private Midi midi;
    private Command controller;

	public MidiLedButton (Midi midi, int getCCId, int setCCId) {
		this.midi = midi;
        setId = setCCId;
        getId = getCCId;
        controller = new StartEndCommand(
			() -> setLed(1),
			() -> setLed(0)
		);
    }

    /**
     * sets the led state
     * @param state 0=off, 1=on, 2=blinking
     */
    public void setLed(int state){
        midi.sendNote(setId, state);
    }
    
    /**
     * gets the state of the button, deafults to off
     */
    @Override
    public boolean get() {
        return midi.getNote(getId);
    }

	public void toggleBind() {
		this.toggleWhenPressed(controller);
	}

	public void pressBind() {
		triggerBind(this);
	}

	public void commandBind(Command command) {
		triggerBind(new Trigger(command::isFinished).negate());
	}

	public void booleanSupplierBind(BooleanSupplier supplier) {
		new Trigger(supplier).whileActiveContinuous(controller);
	}

	private void triggerBind(Trigger trigger) {
		trigger.whileActiveOnce(controller);
	}
}

package frc.robot.oi;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.utilities.Usable;

public class ShuffleboardLEDButton extends Button implements Usable {

	public interface LED {
		public void set(boolean state);
	}

    private Command controller;
    
    private NetworkTableEntry entry;

    private ArrayList<Object> users;

	public ShuffleboardLEDButton(NetworkTableEntry entry){

        this.entry = entry;

        entry.forceSetNumber(0);

        users = new ArrayList<>();

		controller = new StartEndCommand(
			() -> setLed(true),
			() -> setLed(false)
		);
    }
    
    @Override
    public boolean get() {
        //if pressed is true report back and say we saw it being true by making it false agin
        if (getPressed()){
            setPressed(false);
            return true;
        }
        else{
            return false;
        }
    }

    //for later use
    private boolean getLed(){
        int value = entry.getNumber(0).intValue() ;
        int mask = 0b00000000000000000000000000000010;
        //if the second bit is 1 return true
        return (value & mask) == 2;
    }

    private void setLed(boolean set){
        //gets the pressed value so we preserve it
        int oldNumb = entry.getNumber(0).intValue() & 0b00000000000000000000000000000001;
			if(set){
				//takes the pressed bit from the origonal data and adds the new led value
				entry.setNumber(oldNumb | 0b00000000000000000000000000000010);
			}
			else{
			    //takes the pressed bit and adds nothing to it (not nessary but good to have to show what is happning)
                entry.setNumber(oldNumb | 0b00000000000000000000000000000000);
			}
    }

    private boolean getPressed(){
        int value = entry.getNumber(0).intValue() ;
        int mask = 0b00000000000000000000000000000001;
        //if the first bit is 1 return true
        return (value & mask) == 1;
    }

    private void setPressed(boolean set){
        //gets the led value so we can perserve it
        int oldNumb = entry.getNumber(0).intValue() & 0b00000000000000000000000000000010;
			if(set){
				//takes the led bit from the origonal data and adds a 1 last bit to it to make the button on
				entry.setNumber(oldNumb | 0b00000000000000000000000000000001);
			}
			else{
			    //takes the led bit and adds nothing to it (not nessary but good to have to show what is happning)
                entry.setNumber(oldNumb | 0b00000000000000000000000000000000);
			}
    }

    //binding commands
	public void toggleBind() {
		this.toggleWhenPressed(controller);
	}

	public void pressBind() {
		triggerBind(this);
	}

	public void commandBind(Command command) {
		triggerBind(new Trigger(command::isScheduled));
	}

	public void booleanSupplierBind(BooleanSupplier supplier) {
		new Trigger(supplier).whileActiveContinuous(controller);
	}

	private void triggerBind(Trigger trigger) {
		trigger.whileActiveOnce(controller);
    }

    //using things
    @Override
    public void using(Object user) {
        users.add(user);
    }

    @Override
    public void release(Object user) {
        users.remove(user);
    }

    @Override
    public boolean inUse() {
        return !users.isEmpty();
    }
}
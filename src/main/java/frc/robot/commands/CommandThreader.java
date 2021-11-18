// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.Thread.State;

import frc.robot.commandegment.Command;
import frc.robot.commandegment.CommandBase;

public class CommandThreader extends CommandBase {

    private volatile Command command;
    private int period;
    private int priority;

    private Exicutor exicutor;

    /**
     * wrapps a command inside a thred allowing it to be exicuted independently of the sceduler
     * it is important to not use the command in any way after it is passed
     * @param command the command to be wrapped
     * @param period the period in ms for the command to be run at
     * @param priority the java thred priority for the command from 1-10
     * WARNING all setter methods the command calls must be syncronised or ONLY EVER used by the command or bad things will happen
     */
    public CommandThreader(Command command, double period, int priority) {
        this.command = command;
        //converts from ms to ns
        this.period = (int)(period*1_000_000);
        this.priority = priority;

        //addRequirments is dumb and will not take in a set and get requirements returns a set so this sets it directly
        this.m_requirements = command.getRequirements();
    }

    //inits the command and then starts the thred
    @Override
    public void initialize() {
        command.initialize();

        exicutor = new Exicutor(command, period);
        exicutor.setName(Command.class.getName() + " exicution thred");
        //1-10
        exicutor.setPriority(priority);
        exicutor.start();        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        if (!isFinished()) {
            // tells the thread to stop
            exicutor.interrupt();

            boolean stopped = isFinished();

            //waits for the thred to stop
            while(!stopped){
                stopped = isFinished();
            }
        }

        //ends the command
        command.end(interrupted);        
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        //command ends if thred stops
        return exicutor.getState() == State.TERMINATED;
    }

}

class Exicutor extends Thread{
    private volatile Command command;
    private int period;

    protected Exicutor(Command command, int period){
        this.command = command;
        this.period = period;
    }

    @Override
    //gets called when thread starts
    public void run() {
        super.run();

        while (!command.isFinished() && !isInterrupted()) {
            //gets the system time
            long commandStartingTime = System.nanoTime();

            //exicutes the commnad
            command.execute();

            try {
                //sleeps the thred the remaining time so that the exicution period is consistant
                long executeTime = (System.nanoTime() - commandStartingTime);
                long sleepPeriod = period - executeTime;

                if (sleepPeriod < 0) {
                    //print out if the thred overran
                    System.out.println(String.format("Loop overran by %f ms", Math.abs(((double)(sleepPeriod))/1_000_000)));

                } else {
                    //sleeps the thred for the calculated time
                    sleep(sleepPeriod / 1_000_000, (int)(sleepPeriod % 1_000_000));
                }

            } catch (InterruptedException e) {
                // will happen if the command is intrupted and needs to end early
                //no need to handle
                System.out.println(getName() + " was intrupted");
            }
        }
    }
}

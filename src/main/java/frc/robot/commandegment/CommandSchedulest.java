// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandegment;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * The scheduler responsible for running {@link Command}s. A Command-based robot should call {@link
 * CommandSchedulest#run()} on the singleton instance in its periodic block in order to run commands
 * synchronously from the main loop. Subsystems should be registered with the scheduler using {@link
 * CommandSchedulest#registerSubsystem(Subsystem...)} in order for their {@link Subsystem#periodic()}
 * methods to be called and for their default commands to be scheduled.
 */
@SuppressWarnings({"PMD.GodClass", "PMD.TooManyFields"})
public final class CommandSchedulest implements Sendable, AutoCloseable {
  /** The Singleton Instance. */
	private static CommandSchedulest instance;
	
	private static final int deafultCommandPriority = -1;

  /**
   * Returns the Scheduler instance.
   *
   * @return the instance
   */
  public static synchronized CommandSchedulest getInstance() {
    if (instance == null) {
      instance = new CommandSchedulest();
    }
    return instance;
  }

  // A map from commands to their scheduling state.  Also used as a set of the currently-running
  // commands.
  private final Vector<CommandWithPriroty> scheduledCommands = new Vector<CommandWithPriroty>();

  private final Vector<Command> runningCommands = new Vector<Command>();


  //keeps track of the priority for each command

  // // A map from required subsystems to their requiring commands.  Also used as a set of the
  // // currently-required subsystems.
  // private final Map<Subsystem, Command> m_requirements = new LinkedHashMap<>();

  // A map from subsystems registered with the scheduler to their default commands.  Also used
  // as a list of currently-registered subsystems.
  private final Vector<Subsystem> m_subsystems = new Vector<>();

  // The set of currently-registered buttons that will be polled every iteration.
  private final Collection<Runnable> m_buttons = new LinkedHashSet<>();

  private boolean m_disabled;

  // Flag and queues for avoiding ConcurrentModificationException if commands are
  // scheduled/canceled during run
  private boolean m_inRunLoop;
  private final Map<Command, Integer> m_toSchedule = new LinkedHashMap<>();
  private final List<Command> m_toCancel = new ArrayList<>();

  private final Watchdog m_watchdog = new Watchdog(TimedRobot.kDefaultPeriod, () -> {});

  CommandSchedulest() {
    HAL.report(tResourceType.kResourceType_Command, tInstances.kCommand2_Scheduler);
    //sendable BAD
    // SendableRegistry.addLW(this, "Scheduler");
    // LiveWindow.setEnabledListener(
    //     () -> {
    //       disable();
    //       cancelAll();
    //     });
    // LiveWindow.setDisabledListener(
    //     () -> {
    //       enable();
    //     });
  }

  /**
   * Changes the period of the loop overrun watchdog. This should be be kept in sync with the
   * TimedRobot period.
   *
   * @param period Period in seconds.
   */
  public void setPeriod(double period) {
    m_watchdog.setTimeout(period);
  }

  @Override
  public void close() {
    SendableRegistry.remove(this);
    LiveWindow.setEnabledListener(null);
    LiveWindow.setDisabledListener(null);
  }

  /**
   * Adds a button binding to the scheduler, which will be polled to schedule commands.
   *
   * @param button The button to add
   */
  public void addButton(Runnable button) {
    m_buttons.add(button);
  }

  /** Removes all button bindings from the scheduler. */
  public void clearButtons() {
    m_buttons.clear();
  }

  /**
   * Schedules a command for execution. Does nothing if the command is already scheduled. If a
   * command's requirements are not available, it will only be started if all the commands currently
   * using those requirements have been scheduled as interruptible. If this is the case, they will
   * be interrupted and the command will be scheduled.
   *
   * @param interruptible whether this command can be interrupted
   * @param command the command to schedule
   */
  @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
  private void scheduleWithUnlimitedPriority(int priority, Command command) {
    if (m_inRunLoop) {
      m_toSchedule.put(command, priority);
      return;
    }

    if (CommandGroupBase.getGroupedCommands().contains(command)) {
      throw new IllegalArgumentException(
          "A command that is part of a command group cannot be independently scheduled");
    }

    // Do nothing if the scheduler is disabled, the robot is disabled and the command doesn't
    // run when disabled, or the command is already scheduled.
    if (m_disabled
        || (RobotState.isDisabled() && !command.runsWhenDisabled())
        || isScheduled(command)) {
      return;
    }

    initCommand(priority, command);
  }

  /**
   * sceduals a command with a spisfic priority
   * @param priority
   * @param command
   */
  public void scheduleWithPriority(int priority, Command command){
    if(priority < 0){
      throw new IllegalArgumentException("Command priorities can not be less than 0");
    }
    else{
      scheduleWithUnlimitedPriority(priority, command);
    }
  }

  /**
   * Schedules multiple commands for execution. Does nothing if the command is already scheduled. If
   * a command's requirements are not available, it will only be started if all the commands
   * currently using those requirements have been scheduled as interruptible. If this is the case,
   * they will be interrupted and the command will be scheduled.
   *
   * @param interruptible whether the commands should be interruptible
   * @param commands the commands to schedule
   */
  public void schedule(Command... commands) {
    for (Command command : commands) {
      scheduleWithPriority(command.getPriority(), command);
    }
  }

  /**
   * Initializes a given command, adds its requirements to the list, and performs the init actions.
   *
   * @param command The command to initialize
   * @param interruptible Whether the command is interruptible
   * @param requirements The command requirements
   */
  private void initCommand(int priority, Command command) {
    command.initialize();
    CommandWithPriroty commandWithPriroty = new CommandWithPriroty(command, priority);

    m_watchdog.addEpoch(command.getName() + ".initialize()");

    boolean scedualedSucesfull = false;

    //add the command to its place in the map (at the end of the group of commands with the saame priority)
    for(int i = 0; i < scheduledCommands.size(); i++){
      int previousCommandPriority = scheduledCommands.get(i).getPriority();

      //java should be rust
      long nextCommandPriority;
      try{
        nextCommandPriority = (long)scheduledCommands.get(i+1).getPriority();
      }
      catch(Exception e){
        nextCommandPriority = Long.MAX_VALUE;
      }

      //scedual command if apropreate
      if((priority >= previousCommandPriority) && priority < nextCommandPriority){
        scheduledCommands.add(i, commandWithPriroty);
        scedualedSucesfull = true;
        break;
      }
    }
   
    if(!scedualedSucesfull){
        scheduledCommands.add(commandWithPriroty);
    }
  }

  //updates what commands souuld curently be running
  private void updateRunningCommands(){
    Map<Subsystem, Command> commandSubAscotion = new HashMap<>();
    Vector<Command> newRunningCommands = new Vector<>();
    //adds subsystems to map
    for(Subsystem s : m_subsystems){
      commandSubAscotion.put(s, null);
    }
    //moves through commands in reverse order
    for(int i = scheduledCommands.size()-1; i >= 0; i--){
      Command commandToTryToRun = scheduledCommands.get(i).getCommand();

      Set<Subsystem> reqs =  commandToTryToRun.getRequirements();

      //if the command has requirements see if it is acceptable to run
      if(reqs.size()  > 0){
        boolean okToRun = true;
        //checks if command is ok to run
        for(Subsystem s : reqs){
          if(commandSubAscotion.get(s) != null){
            okToRun = false;
            break;
          }
        }

        //if it is then add it to running subsystems and associates its requirements
        if(okToRun){
          for(Subsystem s : reqs){
            commandSubAscotion.replace(s, commandToTryToRun);
          }
          newRunningCommands.addElement(commandToTryToRun);
        }
      }
      else{
        runningCommands.addElement(commandToTryToRun);
      }      
      
    }

    //updates the running command list
    runningCommands.clear();
    for(Command c : newRunningCommands){
      runningCommands.addElement(c);
    }
  }

  /**
   * Runs a single iteration of the scheduler. The execution occurs in the following order:
   *
   * <p>Subsystem periodic methods are called.
   *
   * <p>Button bindings are polled, and new commands are scheduled from them.
   *
   * <p>Currently-scheduled commands are executed.
   *
   * <p>End conditions are checked on currently-scheduled commands, and commands that are finished
   * have their end methods called and are removed.
   *
   * <p>Any subsystems not being used as requirements have their default methods started.
   */
  @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
  public void run() {
    if (m_disabled) {
      return;
    }
    m_watchdog.reset();

    // Run the periodic method of all registered subsystems.
    for (Subsystem subsystem : m_subsystems) {
      subsystem.periodic();
      if (RobotBase.isSimulation()) {
        subsystem.simulationPeriodic();
      }
      m_watchdog.addEpoch(subsystem.getClass().getSimpleName() + ".periodic()");
    }

    // Poll buttons for new commands to add.
    for (Runnable button : m_buttons) {
      button.run();
    }
    m_watchdog.addEpoch("buttons.run()");

		m_inRunLoop = true;

    updateRunningCommands();
		
    // Run scheduled commands, remove finished commands.
    for (Command command : runningCommands) {

      if (!command.runsWhenDisabled() && RobotState.isDisabled()) {
        command.end(true);

        removeCommand(command);
      }

      command.execute();

      m_watchdog.addEpoch(command.getName() + ".execute()");
      if (command.isFinished()) {
        command.end(false);
   
        removeCommand(command);
      }
    }

    m_inRunLoop = false;

    // Schedule/cancel commands from queues populated during loop
    for (Map.Entry<Command, Integer> commandToScedual : m_toSchedule.entrySet()) {
      scheduleWithUnlimitedPriority(commandToScedual.getValue(), commandToScedual.getKey());
    }

    for (Command command : m_toCancel) {
      cancel(command);
    }

    m_toSchedule.clear();
    m_toCancel.clear();

    m_watchdog.disable();
    if (m_watchdog.isExpired()) {
      System.out.println("CommandScheduler loop overrun");
      m_watchdog.printEpochs();
    }
  }

  /**
   * Registers subsystems with the scheduler. This must be called for the subsystem's periodic block
   * to run when the scheduler is run, and for the subsystem's default command to be scheduled. It
   * is recommended to call this from the constructor of your subsystem implementations.
   *
   * @param subsystems the subsystem to register
   */
  public void registerSubsystem(Subsystem... subsystems) {
    for (Subsystem subsystem : subsystems) {
      m_subsystems.add(subsystem);
    }
  }

  /**
   * Un-registers subsystems with the scheduler. The subsystem will no longer have its periodic
   * block called, and will not have its default command scheduled.
   *
   * @param subsystems the subsystem to un-register
   */
  public void unregisterSubsystem(Subsystem... subsystems) {
		for(Subsystem s : subsystems){
			m_subsystems.remove(s);
		}
  }

  /**
   * Sets the default command for a subsystem. Registers that subsystem if it is not already
   * registered. Default commands will run whenever there is no other command currently scheduled
   * that requires the subsystem. Default commands should be written to never end (i.e. their {@link
   * Command#isFinished()} method should return false).
   *
   * @param subsystem the subsystem whose default command will be set
   * @param defaultCommand the default command to associate with the subsystem
   */
  public void setDefaultCommand(Subsystem subsystem, Command defaultCommand) {
    if (!defaultCommand.getRequirements().contains(subsystem)) {
      throw new IllegalArgumentException("Default commands must require their subsystem!");
    }

    if (defaultCommand.isFinished()) {
      throw new IllegalArgumentException("Default commands should not end!");
    }

    scheduleWithUnlimitedPriority(deafultCommandPriority, defaultCommand.perpetually());
  }

  private void removeCommand(Command command){
    for(int i = 0; i<scheduledCommands.size(); i++){
      if(scheduledCommands.get(i).getCommand().equals(command)){
        scheduledCommands.remove(i);
        break;
      }
    }
  }

  /**
   * Cancels commands. The scheduler will only call {@link Command#end(boolean)} method of the
   * canceled command with {@code true}, indicating they were canceled (as opposed to finishing
   * normally).
   *
   * <p>Commands will be canceled even if they are not scheduled as interruptible.
   *
   * @param commands the commands to cancel
   */
  public void cancel(Command... commands) {
    if (m_inRunLoop) {
      m_toCancel.addAll(List.of(commands));
      return;
    }

    //makes sure alreadt stopped commands are not ended twice
    for (Command command : commands) {
      if (!isScheduled(command)) {
        continue;
      }

      command.end(true);

      removeCommand(command);
      m_watchdog.addEpoch(command.getName() + ".end(true)");
    }
  }

  /** Cancels all commands that are currently scheduled. */
  public void cancelAll() {
    for (CommandWithPriroty command : scheduledCommands) {
      cancel(command.getCommand());
    }
	}
	
  /**
   * cancles any commands associated with a subsystem
   * @param subsystem 
   */
	public void cancleCommandsForSubsystem(Subsystem subsystem){
    ArrayList<Command> toCancle = new ArrayList<Command>();
		for(CommandWithPriroty c : scheduledCommands){
      if(c.getCommand().getRequirements().contains(subsystem)){
        toCancle.add(c.getCommand());
      }
    }
    for(Command c : toCancle){
      cancel(c);
    }
	}

  /**
   * Whether the given commands are scedualed. Note that this only works on commands that are directly
   * scheduled by the scheduler; it will not work on commands inside of CommandGroups, as the
   * scheduler does not see them.
   *
   * @param commands the command to query
   * @return whether the command is currently scheduled
   */
  public boolean isScheduled(Command... commands) {
    for (Command c: commands){
      for (CommandWithPriroty cwp: scheduledCommands){
        if (cwp.getCommand().equals(c)){
          return true;
        }
      }
    }
    return false;
    // return scheduledCommands.containsAll(Set.of(commands));
  }

  /**
   * Whether the given commands are curently running. Note that this only works on commands that are directly
   * scheduled by the scheduler; it will not work on commands inside of CommandGroups, as the
   * scheduler does not see them.
   *
   * @param commands the command to query
   * @return whether the command is currently scheduled
   */
  public boolean isRunning(Command... commands) {
    return runningCommands.containsAll(Set.of(commands));
  }

  /** Disables the command scheduler. */
  public void disable() {
    m_disabled = true;
  }

  /** Enables the command scheduler. */
  public void enable() {
    m_disabled = false;
  }

  @Override
  public void initSendable(SendableBuilder builder) {   
    builder.setSmartDashboardType("Scheduler");
    final NetworkTableEntry namesEntry = builder.getEntry("Names");
    final NetworkTableEntry idsEntry = builder.getEntry("Ids");
    final NetworkTableEntry cancelEntry = builder.getEntry("Cancel");
    builder.setUpdateTable(
        () -> {
          if (namesEntry == null || idsEntry == null || cancelEntry == null) {
            return;
          }

          Map<Double, Command> ids = new LinkedHashMap<>();

          for (CommandWithPriroty command : scheduledCommands) {
            ids.put((double) command.getCommand().hashCode(), command.getCommand());
          }

          double[] toCancel = cancelEntry.getDoubleArray(new double[0]);
          if (toCancel.length > 0) {
            for (double hash : toCancel) {
              cancel(ids.get(hash));
              ids.remove(hash);
            }
            cancelEntry.setDoubleArray(new double[0]);
          }

          List<String> names = new ArrayList<>();

          ids.values().forEach(command -> names.add(command.getName()));

          namesEntry.setStringArray(names.toArray(new String[0]));
          idsEntry.setNumberArray(ids.keySet().toArray(new Double[0]));
        });
  }

  private class CommandWithPriroty{
    private int priroty;
    private Command command;

    public CommandWithPriroty(Command command, int priroty){
      this.priroty = priroty;
      this.command = command;
    }

    public int getPriority() {
      return this.priroty;
    }

    public Command getCommand(){
      return this.command;
    }
  }
} 
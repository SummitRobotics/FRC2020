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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.function.Consumer;

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
  private final Map<Command, CommandInfo> scheduledCommands = new LinkedHashMap<>();

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
   * Initializes a given command, adds its requirements to the list, and performs the init actions.
   *
   * @param command The command to initialize
   * @param interruptible Whether the command is interruptible
   * @param requirements The command requirements
   */
  private void initCommand(Command command, int priority, Set<Subsystem> requirements) {
    CommandInfo scheduledCommand = new CommandInfo(priority, requirements);
    scheduledCommands.put(command, scheduledCommand);
    command.initialize();

    m_watchdog.addEpoch(command.getName() + ".initialize()");
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
        || scheduledCommands.containsKey(command)) {
      return;
    }

    Set<Subsystem> requirements = command.getRequirements();

    //TODO scedual the command
  }

  public void scheduleWithPriority(int priority, Command command){
    if(priority < 0){
      throw new IllegalArgumentException("Command priorities can not be less than 1");
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
		
		//TODO make this work

		//sort the commands from highest to lowest priority (probably best to do this when they are scedualed)

		//apply each command to all of its required subsystems so long as no other command is useing them

		//run the command applyed to each subsystem

		//run and commands that dont require subsystems


    // Run scheduled commands, remove finished commands.
    // for (Iterator<Command> iterator = scheduledCommands.keySet().iterator();
    //     iterator.hasNext(); ) {
    //   Command command = iterator.next();

    //   if (!command.runsWhenDisabled() && RobotState.isDisabled()) {
    //     command.end(true);

    //     m_requirements.keySet().removeAll(command.getRequirements());
    //     iterator.remove();
    //     m_watchdog.addEpoch(command.getName() + ".end(true)");
    //     continue;
    //   }

    //   command.execute();

    //   m_watchdog.addEpoch(command.getName() + ".execute()");
    //   if (command.isFinished()) {
    //     command.end(false);
   
    //     iterator.remove();

    //     m_requirements.keySet().removeAll(command.getRequirements());
    //     m_watchdog.addEpoch(command.getName() + ".end(false)");
    //   }
    // }
    m_inRunLoop = false;

    // Schedule/cancel commands from queues populated during loop
    for (Map.Entry<Command, Integer> commandInterruptible : m_toSchedule.entrySet()) {
      scheduleWithUnlimitedPriority(commandInterruptible.getValue(), commandInterruptible.getKey());
    }

    for (Command command : m_toCancel) {
      cancel(command);
    }

    m_toSchedule.clear();
    m_toCancel.clear();

    // Add default commands for un-required registered subsystems.
    // for (Map.Entry<Subsystem, Command> subsystemCommand : m_subsystems.entrySet()) {
    //   if (!m_requirements.containsKey(subsystemCommand.getKey())
    //       && subsystemCommand.getValue() != null) {
    //     schedule(subsystemCommand.getValue());
    //   }
    // }

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
   * Command#isFinished()} method should return false), as they would simply be re-scheduled if they
   * do. Default commands must also require their subsystem.
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

    scheduleWithUnlimitedPriority(deafultCommandPriority, defaultCommand);
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

    for (Command command : commands) {
      if (!scheduledCommands.containsKey(command)) {
        continue;
      }

      command.end(true);

      scheduledCommands.remove(command);
      //m_requirements.keySet().removeAll(command.getRequirements());
      m_watchdog.addEpoch(command.getName() + ".end(true)");
    }
  }

  /** Cancels all commands that are currently scheduled. */
  public void cancelAll() {
    for (Command command : scheduledCommands.keySet().toArray(new Command[0])) {
      cancel(command);
    }
	}
	
	public void cancleCommandsForSubsystem(Subsystem subsystem){
		//TODO make this
	}

  /**
   * Returns the time since a given command was scheduled. Note that this only works on commands
   * that are directly scheduled by the scheduler; it will not work on commands inside of
   * commandgroups, as the scheduler does not see them.
   *
   * @param command the command to query
   * @return the time since the command was scheduled, in seconds
   */
  public double timeSinceScheduled(Command command) {
    CommandInfo commandState = scheduledCommands.get(command);
    if (commandState != null) {
      return commandState.timeSinceInitialized();
    } else {
      return -1;
    }
  }

  /**
   * Whether the given commands are running. Note that this only works on commands that are directly
   * scheduled by the scheduler; it will not work on commands inside of CommandGroups, as the
   * scheduler does not see them.
   *
   * @param commands the command to query
   * @return whether the command is currently scheduled
   */
  public boolean isScheduled(Command... commands) {
    return scheduledCommands.keySet().containsAll(Set.of(commands));
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

          for (Command command : scheduledCommands.keySet()) {
            ids.put((double) command.hashCode(), command);
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
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandegment;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import frc.robot.utilities.PrioritisedInput;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

/** A {@link Sendable} base class for {@link Command}s. */
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class CommandBase implements Sendable, Command {

  protected Set<Subsystem> m_requirements = new HashSet<>();
  protected int priority;
  public Vector<UUID> uuid = new Vector<>();
  private Vector<PrioritisedInput> registeredInputs = new Vector<>();

  protected CommandBase() {
    String name = getClass().getName();
    SendableRegistry.add(this, name.substring(name.lastIndexOf('.') + 1));
    priority = 0;
    this.uuid.add(UUID.randomUUID());
  }

  @Override
  public int getPriority() {
    return priority;
  }

  public int getScedualedPriority(){
    return CommandSchedulest.getInstance().getCommandScedualedPriority(this);
  }
  
  public void setPriority(int priority){
    if(priority < 0){
      throw new IllegalArgumentException("priorities below 0 are not aloud");
    }
    else{
      this.priority = priority;
    }
  }

  /**
   * registers a set of inputs with a spisfic priority
   * @param priority the priority of the inputs
   * @param inputs the inputs to register
   */
  protected void registerAxies(int priority, PrioritisedInput... inputs){
    for(PrioritisedInput i : inputs){
      registeredInputs.add(i);
      i.register(this, priority);
    }
  }

   /**
   * registers a set of inputs with the priority the command is scedulaed with
   * @param inputs the inputs to register
   */
  protected void registerAxies(PrioritisedInput... inputs){
    System.out.println(this.getScedualedPriority());
    registerAxies(this.getScedualedPriority(), inputs);
  }

  /**
   * relices all registered axies
   */
  protected void reliceAxies(){
    for(PrioritisedInput i : registeredInputs){
      i.release(this);
    }
    registeredInputs.clear();
  }

  /**
   * relices all inputs passed in
   * @param inputs the inputs to relice
   */
  protected void reliceAxies(PrioritisedInput... inputs){
    for(PrioritisedInput i : inputs){
      registeredInputs.remove(i);
      i.release(this);
    }
  }


  /**
   * Adds the specified requirements to the command.
   *
   * @param requirements the requirements to add
   */
  public final void addRequirements(Subsystem... requirements) {
    m_requirements.addAll(Set.of(requirements));
  }

  @Override
  public Set<Subsystem> getRequirements() {
    return m_requirements;
  }

  @Override
  public String getName() {
    return SendableRegistry.getName(this);
  }

  /**
   * Sets the name of this Command.
   *
   * @param name name
   */
  @Override
  public void setName(String name) {
    SendableRegistry.setName(this, name);
  }

  /**
   * Decorates this Command with a name. Is an inline function for #setName(String);
   *
   * @param name name
   * @return the decorated Command
   */
  public CommandBase withName(String name) {
    this.setName(name);
    return this;
  }

  /**
   * Gets the subsystem name of this Command.
   *
   * @return Subsystem name
   */
  @Override
  public String getSubsystem() {
    return SendableRegistry.getSubsystem(this);
  }

  /**
   * Sets the subsystem name of this Command.
   *
   * @param subsystem subsystem name
   */
  @Override
  public void setSubsystem(String subsystem) {
    SendableRegistry.setSubsystem(this, subsystem);
  }

  /**
   * Initializes this sendable. Useful for allowing implementations to easily extend SendableBase.
   *
   * @param builder the builder used to construct this sendable
   */
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Command");
    builder.addStringProperty(".name", this::getName, null);
    builder.addBooleanProperty(
        "running",
        this::isScheduled,
        value -> {
          if (value) {
            if (!isScheduled()) {
              schedule();
            }
          } else {
            if (isScheduled()) {
              cancel();
            }
          }
        });
    builder.addBooleanProperty(
        ".isParented", () -> CommandGroupBase.getGroupedCommands().contains(this), null);
  }

  @Override
  public boolean equalsUUID(Object command){
      try{
          CommandBase cmd = ((CommandBase)command);
          for (int i = 0; i < cmd.getUUID().size(); i++){
              if (this.uuid.contains(cmd.getUUID().get(i))){
                  return true;
              }
          }
          return false;

      } catch (Exception e){
          return false;
      }
  }

  protected void addUuids(Vector<UUID>... uuids){
    for(int i = 0; i<uuids.length; i++){
        this.uuid.addAll(uuids[i]);
    }
  }


  @Override
  public Vector<UUID> getUUID(){
      return this.uuid;
  }
}

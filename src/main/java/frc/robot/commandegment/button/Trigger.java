// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandegment.button;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

import frc.robot.commandegment.Command;
import frc.robot.commandegment.CommandSchedulest;
import frc.robot.commandegment.InstantCommand;
import frc.robot.commandegment.Subsystem;
import java.util.function.BooleanSupplier;

/**
 * This class provides an easy way to link commands to inputs.
 *
 * <p>It is very easy to link a button to a command. For instance, you could link the trigger button
 * of a joystick to a "score" command.
 *
 * <p>It is encouraged that teams write a subclass of Trigger if they want to have something unusual
 * (for instance, if they want to react to the user holding a button while the robot is reading a
 * certain sensor input). For this, they only have to write the {@link Trigger#get()} method to get
 * the full functionality of the Trigger class.
 */
public class Trigger {
  private final BooleanSupplier m_isActive;

  /**
   * Creates a new trigger with the given condition determining whether it is active.
   *
   * @param isActive returns whether or not the trigger should be active
   */
  public Trigger(BooleanSupplier isActive) {
    m_isActive = isActive;
  }

  /**
   * Creates a new trigger that is always inactive. Useful only as a no-arg constructor for
   * subclasses that will be overriding {@link Trigger#get()} anyway.
   */
  public Trigger() {
    m_isActive = () -> false;
  }

  /**
   * Returns whether or not the trigger is active.
   *
   * <p>This method will be called repeatedly a command is linked to the Trigger.
   *
   * @return whether or not the trigger condition is active.
   */
  public boolean get() {
    return m_isActive.getAsBoolean();
  }

  /**
   * Starts the given command whenever the trigger just becomes active.
   *
   * @param command the command to start
   * @param interruptible whether the command is interruptible
   * @return this trigger, so calls can be chained
   */
  public Trigger whenActive(final Command command) {
    requireNonNullParam(command, "command", "whenActive");

    CommandSchedulest.getInstance()
        .addButton(
            new Runnable() {
              private boolean m_pressedLast = get();

              @Override
              public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                  command.schedule();
                }

                m_pressedLast = pressed;
              }
            });

    return this;
  }

  /**
   * Runs the given runnable whenever the trigger just becomes active.
   *
   * @param toRun the runnable to run
   * @param requirements the required subsystems
   * @return this trigger, so calls can be chained
   */
  public Trigger whenActive(final Runnable toRun, Subsystem... requirements) {
    return whenActive(new InstantCommand(toRun, requirements));
  }

  /**
   * Constantly starts the given command while the button is held.
   *
   * <p>{@link Command#schedule(boolean)} will be called repeatedly while the trigger is active, and
   * will be canceled when the trigger becomes inactive.
   *
   * @param command the command to start
   * @param interruptible whether the command is interruptible
   * @return this trigger, so calls can be chained
   */
  public Trigger whileActiveContinuous(final Command command) {
    requireNonNullParam(command, "command", "whileActiveContinuous");

    CommandSchedulest.getInstance()
        .addButton(
            new Runnable() {
              private boolean m_pressedLast = get();

              @Override
              public void run() {
                boolean pressed = get();

                if (pressed) {
                  command.schedule();
                } else if (m_pressedLast) {
                  command.cancel();
                }

                m_pressedLast = pressed;
              }
            });
    return this;
  }

  /**
   * Constantly runs the given runnable while the button is held.
   *
   * @param toRun the runnable to run
   * @param requirements the required subsystems
   * @return this trigger, so calls can be chained
   */
  public Trigger whileActiveContinuous(final Runnable toRun, Subsystem... requirements) {
    return whileActiveContinuous(new InstantCommand(toRun, requirements));
  }

  /**
   * Starts the given command when the trigger initially becomes active, and ends it when it becomes
   * inactive, but does not re-start it in-between.
   *
   * @param command the command to start
   * @param interruptible whether the command is interruptible
   * @return this trigger, so calls can be chained
   */
  public Trigger whileActiveOnce(final Command command) {
    requireNonNullParam(command, "command", "whileActiveOnce");

    CommandSchedulest.getInstance()
        .addButton(
            new Runnable() {
              private boolean m_pressedLast = get();

              @Override
              public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                  command.schedule();
                } else if (m_pressedLast && !pressed) {
                  command.cancel();
                }

                m_pressedLast = pressed;
              }
            });
    return this;
  }

  /**
   * Starts the command when the trigger becomes inactive.
   *
   * @param command the command to start
   * @param interruptible whether the command is interruptible
   * @return this trigger, so calls can be chained
   */
  public Trigger whenInactive(final Command command) {
    requireNonNullParam(command, "command", "whenInactive");

    CommandSchedulest.getInstance()
        .addButton(
            new Runnable() {
              private boolean m_pressedLast = get();

              @Override
              public void run() {
                boolean pressed = get();

                if (m_pressedLast && !pressed) {
                  command.schedule();
                }

                m_pressedLast = pressed;
              }
            });
    return this;
  }

  /**
   * Runs the given runnable when the trigger becomes inactive.
   *
   * @param toRun the runnable to run
   * @param requirements the required subsystems
   * @return this trigger, so calls can be chained
   */
  public Trigger whenInactive(final Runnable toRun, Subsystem... requirements) {
    return whenInactive(new InstantCommand(toRun, requirements));
  }

  /**
   * Toggles a command when the trigger becomes active.
   *
   * @param command the command to toggle
   * @param interruptible whether the command is interruptible
   * @return this trigger, so calls can be chained
   */
  public Trigger toggleWhenActive(final Command command) {
    requireNonNullParam(command, "command", "toggleWhenActive");

    CommandSchedulest.getInstance()
        .addButton(
            new Runnable() {
              private boolean m_pressedLast = get();

              @Override
              public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                  if (command.isScheduled()) {
                    command.cancel();
                  } else {
                    command.schedule();
                  }
                }

                m_pressedLast = pressed;
              }
            });
    return this;
  }

  /**
   * Cancels a command when the trigger becomes active.
   *
   * @param command the command to cancel
   * @return this trigger, so calls can be chained
   */
  public Trigger cancelWhenActive(final Command command) {
    requireNonNullParam(command, "command", "cancelWhenActive");

    CommandSchedulest.getInstance()
        .addButton(
            new Runnable() {
              private boolean m_pressedLast = get();

              @Override
              public void run() {
                boolean pressed = get();

                if (!m_pressedLast && pressed) {
                  command.cancel();
                }

                m_pressedLast = pressed;
              }
            });
    return this;
  }

  /**
   * Composes this trigger with another trigger, returning a new trigger that is active when both
   * triggers are active.
   *
   * @param trigger the trigger to compose with
   * @return the trigger that is active when both triggers are active
   */
  public Trigger and(Trigger trigger) {
    return new Trigger(() -> get() && trigger.get());
  }

  /**
   * Composes this trigger with another trigger, returning a new trigger that is active when either
   * trigger is active.
   *
   * @param trigger the trigger to compose with
   * @return the trigger that is active when either trigger is active
   */
  public Trigger or(Trigger trigger) {
    return new Trigger(() -> get() || trigger.get());
  }

  /**
   * Creates a new trigger that is active when this trigger is inactive, i.e. that acts as the
   * negation of this trigger.
   *
   * @return the negated trigger
   */
  public Trigger negate() {
    return new Trigger(() -> !get());
  }
}

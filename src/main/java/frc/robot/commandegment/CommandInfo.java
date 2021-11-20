// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commandegment;

import java.util.Set;
import java.util.Vector;

import edu.wpi.first.wpilibj.Timer;

/**
 * Class that holds scheduling state for a command. Used internally by the {@link CommandSchedulest}.
 */
class CommandInfo {
  // The time since this command was initialized.
  private double m_startTime = -1;

  private int priority;

  private Vector<Subsystem> requirements = new Vector<>();

  CommandInfo(int priority, Set<Subsystem> requirements) {
    this.priority = priority;
    this.requirements.addAll(requirements);
    startTiming();
    startRunning();
  }

  private void startTiming() {
    m_startTime = Timer.getFPGATimestamp();
  }

  synchronized void startRunning() {
    m_startTime = -1;
  }

  int getPriority(){
    return priority;
  }

  void setPriority(int priority){
    this.priority = priority;
  }

  Vector<Subsystem> getRequirements(){
    return requirements;
  }

  double timeSinceInitialized() {
    return m_startTime != -1 ? Timer.getFPGATimestamp() - m_startTime : -1;
  }
}

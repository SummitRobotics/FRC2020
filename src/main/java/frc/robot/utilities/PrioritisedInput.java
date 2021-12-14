// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utilities;

/** Add your docs here. */
public interface PrioritisedInput {
    public void register(Object user, int Priority);
    public void release(Object user);
    public boolean ableToUse(Object user);
}

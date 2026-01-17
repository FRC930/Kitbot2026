// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.launcher;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Sets the controless the launcher and endexer */
public class LauncherSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private LauncherIO m_IO;

  public LauncherSubsystem(LauncherIO IO) {
    m_IO = IO;
  }
  /**
   * Sets the speed for the Launcher
   *
   * @param speed
   */
  public void setLaunchSpeed(Voltage speed) {
    m_IO.setLauncherTarget(speed);
  }
  /**
   * Sets the speed for the Indexer
   *
   * @param speed
   */
  public void setIndexerSpeed(Voltage speed) {
    m_IO.setIndexerTarget(speed);
  }
}

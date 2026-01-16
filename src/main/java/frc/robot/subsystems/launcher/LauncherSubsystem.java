// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.launcher;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Sets the controless the launcher and endexer */
public class LauncherSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  TalonFX launcherMoter;

  TalonFX indexerMoter;

  public LauncherSubsystem(
      int launcherMotorID,
      String launcherMotorCanBus,
      int indexerMotorID,
      String indexerMotorCanBus) {
    launcherMoter = new TalonFX(launcherMotorID, launcherMotorCanBus);
    indexerMoter = new TalonFX(indexerMotorID, indexerMotorCanBus);
  }
  /**
   * Sets the speed for the Launcher
   *
   * @param speed
   */
  public void setLaunchSpeed(double speed) {
    launcherMoter.set(speed);
  }
  /**
   * Sets the speed for the Indexe
   *
   * @param speed
   */
  public void setIndexeSpeed(double speed) {
    indexerMoter.set(speed);
  }
}

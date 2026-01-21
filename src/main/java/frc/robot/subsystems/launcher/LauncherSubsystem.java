// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.util.EnumState;
import org.littletonrobotics.junction.Logger;

/** Sets the controless the launcher and endexer */
public class LauncherSubsystem extends SubsystemBase implements LauncherEvents {
  /** Creates a new ExampleSubsystem. */
  private LauncherIO m_IO;

  private final EnumState<LauncherState> currentGoal =
      new EnumState<>("Launcher/States", LauncherState.IDLE);

  private LauncherInputsAutoLogged logged = new LauncherInputsAutoLogged();

  public LauncherSubsystem(LauncherIO IO) {
    m_IO = IO;
    logged.launcherAngularVelocity = DegreesPerSecond.mutable(0);
    logged.launcherVoltage = Volts.mutable(0);
    logged.launcherSetVoltage = Volts.mutable(0);
    logged.indexerAngularVelocity = DegreesPerSecond.mutable(0);
    logged.indexerVoltage = Volts.mutable(0);
    logged.indexerSetVoltage = Volts.mutable(0);
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

  public Command launchCommand() {
    return runOnce(
        () -> {
          setIndexerSpeed(Volts.of(4.0));
          setLaunchSpeed(Volts.of(11.0));
        });
  }

  public Command idleCommand() {
    return runOnce(
        () -> {
          setIndexerSpeed(Volts.of(0));
          setLaunchSpeed(Volts.of(0));
        });
  }

  public void stop() {
    m_IO.stop();
  }

  @Override
  public void periodic() {
    m_IO.updateInputs(logged);
    Logger.processInputs("RobotState/Launcher", logged);
  }

  @Override
  public Trigger isIdleTrigger() {
    return currentGoal.is(LauncherState.IDLE);
  }

  @Override
  public Trigger isLaunchingTrigger() {
    return currentGoal.is(LauncherState.LAUNCHING);
  }
}

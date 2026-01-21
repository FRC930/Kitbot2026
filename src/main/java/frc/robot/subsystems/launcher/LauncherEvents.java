package frc.robot.subsystems.launcher;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface LauncherEvents {
  public Trigger isIdleTrigger();

  public Trigger isLaunchingTrigger();
}

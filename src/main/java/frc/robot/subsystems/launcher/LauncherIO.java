package frc.robot.subsystems.launcher;

import edu.wpi.first.units.measure.Voltage;

public interface LauncherIO {
  public void setLauncherTarget(Voltage target);

  public void stop();

  public void setIndexerTarget(Voltage target);
}

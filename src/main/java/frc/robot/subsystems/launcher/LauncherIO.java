package frc.robot.subsystems.launcher;

import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import org.littletonrobotics.junction.AutoLog;

public interface LauncherIO {
  @AutoLog
  public static class LauncherInputs {
    public MutAngularVelocity launcherAngularVelocity;
    public MutVoltage launcherVoltage;
    public MutVoltage launcherSetVoltage;
    public MutAngularVelocity indexerAngularVelocity;
    public MutVoltage indexerVoltage;
    public MutVoltage indexerSetVoltage;
  }

  public void setLauncherTarget(Voltage target);

  public void stop();

  public void setIndexerTarget(Voltage target);

  public void updateInputs(LauncherInputs input);
}

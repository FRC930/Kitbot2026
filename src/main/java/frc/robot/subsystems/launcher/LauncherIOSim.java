package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;

public class LauncherIOSim implements LauncherIO {
  private Voltage launcherAppliedVoltage = Volts.mutable(0.0);
  private Voltage indexerAppliedVoltage = Volts.mutable(0.0);

  private final FlywheelSim launcherSim;
  private final FlywheelSim indexerSim;

  public LauncherIOSim() {
    launcherSim =
        new FlywheelSim(
            LinearSystemId.createFlywheelSystem(DCMotor.getKrakenX60Foc(1), 0.0005, 1),
            DCMotor.getKrakenX60Foc(1),
            0.01);
    indexerSim =
        new FlywheelSim(
            LinearSystemId.createFlywheelSystem(DCMotor.getKrakenX60Foc(1), 0.0005, 1),
            DCMotor.getKrakenX60Foc(1),
            0.01);
  }

  @Override
  public void setLauncherTarget(Voltage target) {
    this.launcherAppliedVoltage = target;
  }

  @Override
  public void stop() {
    setLauncherTarget(Volts.of(0));
    setIndexerTarget(Volts.of(0));
  }

  @Override
  public void setIndexerTarget(Voltage target) {
    this.indexerAppliedVoltage = target;
  }

  @Override
  public void updateInputs(LauncherInputs input) {
    input.launcherAngularVelocity.mut_replace(launcherSim.getAngularVelocity());
    input.launcherSetVoltage.mut_replace(launcherAppliedVoltage);
    input.indexerAngularVelocity.mut_replace(indexerSim.getAngularVelocity());
    input.indexerSetVoltage.mut_replace(indexerAppliedVoltage);

    // Periodic
    launcherSim.setInputVoltage(launcherAppliedVoltage.in(Volts));
    launcherSim.update(0.02);
    indexerSim.setInputVoltage(indexerAppliedVoltage.in(Volts));
    indexerSim.update(0.02);
  }
}

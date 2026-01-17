package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Voltage;
import frc.robot.util.CANDef;


public class LauncherIOTalonFX implements LauncherIO {
  TalonFX launcherMotor;

  TalonFX indexerMotor;

  public LauncherIOTalonFX(CANDef launcherMotorCAN, CANDef indexerMotorCAN) {
    launcherMotor = new TalonFX(launcherMotorCAN.id(), launcherMotorCAN.bus());
    indexerMotor = new TalonFX(indexerMotorCAN.id(), indexerMotorCAN.bus());
    configureTalons();
  }
private void configureTalons() {
    TalonFXConfiguration configLauncher = new TalonFXConfiguration();
    configLauncher.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    configLauncher.CurrentLimits.StatorCurrentLimit = 80.0;
    configLauncher.CurrentLimits.StatorCurrentLimitEnable = true;
    configLauncher.CurrentLimits.SupplyCurrentLimit = 10.0;
    configLauncher.CurrentLimits.SupplyCurrentLimitEnable = true;
    configLauncher.Voltage.PeakForwardVoltage = 16.0;
    configLauncher.Voltage.PeakReverseVoltage = 16.0;
    configLauncher.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    launcherMotor.getConfigurator().apply(configLauncher);

    TalonFXConfiguration configIndexer = new TalonFXConfiguration();
    configIndexer.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    configIndexer.CurrentLimits.StatorCurrentLimit = 80.0;
    configIndexer.CurrentLimits.StatorCurrentLimitEnable = true;
    configIndexer.CurrentLimits.SupplyCurrentLimit = 10.0;
    configIndexer.CurrentLimits.SupplyCurrentLimitEnable = true;
    configIndexer.Voltage.PeakForwardVoltage = 16.0;
    configIndexer.Voltage.PeakReverseVoltage = 16.0;
    configIndexer.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    indexerMotor.getConfigurator().apply(configIndexer);
}
  @Override
  public void setLauncherTarget(Voltage target) {

    launcherMotor.set(target.in(Volts));
  }

  @Override
  public void stop() {
    setLauncherTarget(Volts.of(0));
    setIndexerTarget(Volts.of(0));
  }

  @Override
  public void setIndexerTarget(Voltage target) {
    indexerMotor.set(target.in(Volts));
  }
}

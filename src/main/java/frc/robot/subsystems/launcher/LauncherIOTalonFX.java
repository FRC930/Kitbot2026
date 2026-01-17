package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;

public class LauncherIOTalonFX implements LauncherIO {
  TalonFX launcherMotor;

  TalonFX indexerMotor;

  private VoltageOut launcherRequest;
  private Voltage launcherSetPoint = Volts.of(0);
  private VoltageOut indexerRequest;
  private Voltage indexerSetPoint = Volts.of(0);

  public LauncherIOTalonFX(int launcherMotorCAN, int indexerMotorCAN, CANBus canbus) {
    launcherMotor = new TalonFX(launcherMotorCAN, canbus);
    indexerMotor = new TalonFX(indexerMotorCAN, canbus);
    configureTalons();
  }

  private void configureTalons() {
    TalonFXConfiguration configLauncher = new TalonFXConfiguration();
    configLauncher.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    configLauncher.CurrentLimits.StatorCurrentLimit = 80.0;
    configLauncher.CurrentLimits.StatorCurrentLimitEnable = true;
    configLauncher.CurrentLimits.SupplyCurrentLimit = 10.0;
    configLauncher.CurrentLimits.SupplyCurrentLimitEnable = true;
    configLauncher.Voltage.PeakForwardVoltage = 16.0;
    configLauncher.Voltage.PeakReverseVoltage = 16.0;
    configLauncher.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    launcherMotor.getConfigurator().apply(configLauncher);

    TalonFXConfiguration configIndexer = new TalonFXConfiguration();
    configIndexer.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    configIndexer.CurrentLimits.StatorCurrentLimit = 80.0;
    configIndexer.CurrentLimits.StatorCurrentLimitEnable = true;
    configIndexer.CurrentLimits.SupplyCurrentLimit = 10.0;
    configIndexer.CurrentLimits.SupplyCurrentLimitEnable = true;
    configIndexer.Voltage.PeakForwardVoltage = 16.0;
    configIndexer.Voltage.PeakReverseVoltage = 16.0;
    configIndexer.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    indexerMotor.getConfigurator().apply(configIndexer);
  }

  @Override
  public void setLauncherTarget(Voltage target) {
    launcherRequest = launcherRequest.withOutput(target);
    launcherMotor.setControl(launcherRequest);
    launcherSetPoint = target;
    // launcherMotor.set(target.in(Volts));
  }

  @Override
  public void stop() {
    setLauncherTarget(Volts.of(0));
    setIndexerTarget(Volts.of(0));
  }

  @Override
  public void setIndexerTarget(Voltage target) {
    indexerRequest = indexerRequest.withOutput(target);
    indexerMotor.setControl(indexerRequest);
    indexerSetPoint = target;
    // indexerMotor.set(target.in(Volts));
  }

  @Override
  public void updateInputs(LauncherInputs inputs) {
    inputs.launcherAngularVelocity.mut_replace(launcherMotor.getVelocity().getValue());
    inputs.launcherVoltage.mut_replace(launcherMotor.getMotorVoltage().getValue());
    inputs.launcherSetVoltage.mut_replace(launcherSetPoint);
    inputs.indexerAngularVelocity.mut_replace(indexerMotor.getVelocity().getValue());
    inputs.indexerVoltage.mut_replace(indexerMotor.getMotorVoltage().getValue());
    inputs.indexerSetVoltage.mut_replace(indexerSetPoint);
  }
}

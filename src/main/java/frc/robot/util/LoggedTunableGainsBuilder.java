package frc.robot.util;

import java.util.function.Consumer;

/**
 * Combines LoggedTunableNumber with Gains for real-time PID tuning from SmartDashboard.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * // In your subsystem constants or constructor:
 * LoggedTunableGainsBuilder tunableGains = new LoggedTunableGainsBuilder(
 *     "Launcher/",
 *     1.0, 0.0, 0.1,  // kP, kI, kD
 *     0.0, 0.0, 0.12, 0.0,  // kS, kG, kV, kA
 *     0, 0, 0, 0, 0  // Motion Magic params
 * );
 *
 * // In your periodic method:
 * tunableGains.ifGainsHaveChanged(gains -> {
 *     motor.getConfigurator().apply(
 *         new Slot0Configs()
 *             .withKP(gains.kP)
 *             .withKI(gains.kI)
 *             .withKD(gains.kD)
 *     );
 * });
 * }</pre>
 */
public class LoggedTunableGainsBuilder {
    private LoggedTunableNumber kP;
    private LoggedTunableNumber kI;
    private LoggedTunableNumber kD;
    private LoggedTunableNumber kS;
    private LoggedTunableNumber kG;
    private LoggedTunableNumber kV;
    private LoggedTunableNumber kA;

    private LoggedTunableNumber kMMV;
    private LoggedTunableNumber kMMA;
    private LoggedTunableNumber kMMJ;
    private LoggedTunableNumber kMMEV;
    private LoggedTunableNumber kMMEA;

    public LoggedTunableGainsBuilder(
            String key,
            double kP,
            double kI,
            double kD,
            double kS,
            double kG,
            double kV,
            double kA,
            double kMMV,
            double kMMA,
            double kMMJ,
            double kMMEV,
            double kMMEA) {
        this.kP = new LoggedTunableNumber(key + "kP", kP);
        this.kI = new LoggedTunableNumber(key + "kI", kI);
        this.kD = new LoggedTunableNumber(key + "kD", kD);
        this.kS = new LoggedTunableNumber(key + "kS", kS);
        this.kG = new LoggedTunableNumber(key + "kG", kG);
        this.kV = new LoggedTunableNumber(key + "kV", kV);
        this.kA = new LoggedTunableNumber(key + "kA", kA);
        this.kMMV = new LoggedTunableNumber(key + "kMMV", kMMV);
        this.kMMA = new LoggedTunableNumber(key + "kMMA", kMMA);
        this.kMMJ = new LoggedTunableNumber(key + "kMMJ", kMMJ);
        this.kMMEV = new LoggedTunableNumber(key + "kMMEV", kMMEV);
        this.kMMEA = new LoggedTunableNumber(key + "kMMEA", kMMEA);
    }

    /**
     * Calls the consumer with the current gains if any gain value has changed since last check.
     *
     * @param gainsConsumer Callback to apply the new gains (e.g., update motor configuration)
     */
    public void ifGainsHaveChanged(Consumer<Gains> gainsConsumer) {
        LoggedTunableNumber.ifChanged(
                hashCode(),
                () -> {
                    gainsConsumer.accept(build());
                },
                kP,
                kI,
                kD,
                kS,
                kG,
                kV,
                kA,
                kMMV,
                kMMA,
                kMMJ,
                kMMEV,
                kMMEA);
    }

    /** Build the current Gains from all tunable values. */
    public Gains build() {
        return Gains.builder()
                .kP(kP.get())
                .kI(kI.get())
                .kD(kD.get())
                .kS(kS.get())
                .kG(kG.get())
                .kV(kV.get())
                .kA(kA.get())
                .kMMV(kMMV.get())
                .kMMA(kMMA.get())
                .kMMJ(kMMJ.get())
                .kMMEV(kMMEV.get())
                .kMMEA(kMMEA.get())
                .build();
    }
}

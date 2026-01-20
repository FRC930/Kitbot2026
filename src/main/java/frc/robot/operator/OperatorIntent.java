package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Interprets driver controller inputs into intent triggers.
 *
 * This class has NO state - it just wraps controller buttons
 * and provides semantic meaning (wantsToScore vs rightTrigger).
 *
 * State lives in RobotGoals. This class just says what the
 * operator is physically doing right now.
 *
 * <p>TODO (students): Map your controller buttons to semantic intents here
 */
public class OperatorIntent implements OperatorIntentEvents {

    private final CommandXboxController driver;

    public OperatorIntent(int driverPort) {
        this.driver = new CommandXboxController(driverPort);
    }

    @Override
    public Trigger wantsToScoreTrigger() {
        return driver.b();
    }

    public CommandXboxController getDriver() {
        return driver;
    }

    @Override
    public Trigger wantsToOutake() {
        return driver.a();
    }
}

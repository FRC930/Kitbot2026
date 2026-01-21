package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Interface exposing only the triggers that behaviors might need from OperatorIntent. Keeps the
 * contract minimal - behaviors shouldn't know about raw button inputs.
 *
 * <p>TODO (students): Add intent triggers for your robot's actions
 */
public interface OperatorIntentEvents {
  Trigger wantsToScoreTrigger();

  Trigger wantsToOutake();
}

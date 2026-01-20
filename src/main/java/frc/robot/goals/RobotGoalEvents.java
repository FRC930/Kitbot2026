package frc.robot.goals;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Interface exposing robot goal triggers for behaviors to react to.
 * Behaviors should only depend on this interface, not on RobotGoals directly.
 *
 * <p>TODO (students): Add triggers for each goal in RobotGoal enum
 */
public interface RobotGoalEvents {
    Trigger isIdleTrigger();
    Trigger isLaunchingTrigger();
}

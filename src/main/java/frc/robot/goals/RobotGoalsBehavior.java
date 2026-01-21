package frc.robot.goals;

import frc.robot.operator.OperatorIntentEvents;
import frc.robot.util.GoalBehavior;

/**
 * Wires operator button presses to robot goal state.
 *
 * <p>This is the teleop-specific logic. Autonomous bypasses this and calls RobotGoals.setGoal()
 * directly.
 *
 * <p>TODO (students): Map your button intents to goals here Example:
 * intent.wantsToScore().onTrue(goals.setGoal(RobotGoal.LAUNCHING))
 * .onFalse(goals.setGoal(RobotGoal.IDLE));
 */
public class RobotGoalsBehavior extends GoalBehavior {

  private RobotGoals goals;

  public RobotGoalsBehavior(RobotGoals goals) {
    this.goals = goals;
  }

  @Override
  public void configure(OperatorIntentEvents intent) {
    intent
        .wantsToScoreTrigger()
        .whileTrue(goals.setGoalCommand(RobotGoal.LAUNCHING))
        .whileFalse(goals.setGoalCommand(RobotGoal.IDLE));
  }
}

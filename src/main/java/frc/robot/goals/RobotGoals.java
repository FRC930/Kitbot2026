package frc.robot.goals;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.util.EnumState;
import frc.robot.util.VirtualSubsystem;

/**
 * Central robot state: what we're doing (goal).
 *
 * <p>Can be set by: - Teleop via RobotGoalsBehavior reacting to OperatorIntent - Autonomous via
 * direct command calls
 *
 * <p>TODO (students): Add goal triggers for each value in RobotGoal enum
 */
public class RobotGoals extends VirtualSubsystem implements RobotGoalEvents {

  private final EnumState<RobotGoal> currentGoal =
      new EnumState<>("RobotGoals/Goal", RobotGoal.IDLE);

  public RobotGoals() {}

  public Command setGoalCommand(RobotGoal goal) {
    return Commands.runOnce(() -> currentGoal.set(goal));
  }

  @Override
  public Trigger isIdleTrigger() {
    return currentGoal.is(RobotGoal.IDLE);
  }

  public RobotGoal getCurrentGoal() {
    return currentGoal.get();
  }

  @Override
  public void periodic() {}

  @Override
  public Trigger isLaunchingTrigger() {
    return currentGoal.is(RobotGoal.LAUNCHING);
  }
}

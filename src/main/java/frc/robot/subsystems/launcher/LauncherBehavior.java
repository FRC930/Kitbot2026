package frc.robot.subsystems.launcher;

import frc.robot.goals.RobotGoalEvents;
import frc.robot.state.MatchStateEvents;
import frc.robot.util.SubsystemBehavior;

public class LauncherBehavior extends SubsystemBehavior {

  private final LauncherSubsystem launcher;

  public LauncherBehavior(LauncherSubsystem launcher) {
    this.launcher = launcher;
  }

  @Override
  public void configure(
      RobotGoalEvents goals, MatchStateEvents matchState, LauncherEvents launcherState) {
    goals.isLaunchingTrigger().whileTrue(launcher.launchCommand());
    goals.isIdleTrigger().whileTrue(launcher.idleCommand());
  }
}

package frc.robot.util;

import frc.robot.operator.OperatorIntentEvents;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for goal-level behaviors that react to operator intent.
 * These behaviors translate operator intent into robot goal state changes.
 *
 * <p>Goal behaviors listen to the UI layer (OperatorIntent) and drive the
 * API layer (RobotGoals state).
 *
 * <p>Usage:
 * <pre>{@code
 * public class RobotGoalsBehavior extends GoalBehavior {
 *     private final RobotGoals goals;
 *
 *     public RobotGoalsBehavior(RobotGoals goals) {
 *         super();
 *         this.goals = goals;
 *     }
 *
 *     @Override
 *     public void configure(OperatorIntent intent, RobotGoalEvents goals) {
 *         intent.wantsToScore()
 *             .onTrue(this.goals.setGoal(RobotGoal.SCORING))
 *             .onFalse(this.goals.setGoal(RobotGoal.IDLE));
 *     }
 * }
 * }</pre>
 */
public abstract class GoalBehavior extends Behavior<GoalBehavior> {

    private static final List<GoalBehavior> goalBehaviors = new ArrayList<>();

    protected GoalBehavior() {
        super();
        goalBehaviors.add(this);
    }

    /**
     * Configure all registered goal behaviors.
     *
     * @param intent The operator intent to react to
     * @param goals The goal events interface (for reading current goals if needed)
     */
    public static void configureAll(OperatorIntentEvents intent) {
        for (GoalBehavior behavior : goalBehaviors) {
            behavior.configure(intent);
        }
    }

    /**
     * Configure this goal behavior's trigger bindings.
     *
     * @param intent The operator intent to react to
     * @param goals The goal events interface (for reading current goals if needed)
     */
    public abstract void configure(OperatorIntentEvents intent);
}

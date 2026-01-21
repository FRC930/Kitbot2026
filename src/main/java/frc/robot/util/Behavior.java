package frc.robot.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all behaviors in the reactive architecture. Behaviors are self-registering and
 * configure trigger-based reactions.
 *
 * <p>This is the foundation for both:
 *
 * <ul>
 *   <li>{@link SubsystemBehavior} - Hardware behaviors that react to robot goals
 *   <li>{@link GoalBehavior} - Goal behaviors that react to operator intent
 * </ul>
 *
 * <p>All behaviors auto-register when constructed and can be configured in batch.
 *
 * @param <T> The type of this behavior (for fluent registration)
 */
public abstract class Behavior<T extends Behavior<T>> {

  private static final List<Behavior<?>> allBehaviors = new ArrayList<>();

  protected Behavior() {
    allBehaviors.add(this);
  }

  /** Get all registered behaviors of any type. */
  public static List<Behavior<?>> getAllBehaviors() {
    return new ArrayList<>(allBehaviors);
  }

  /** Clear all registered behaviors (useful for testing). */
  public static void clearAll() {
    allBehaviors.clear();
  }
}

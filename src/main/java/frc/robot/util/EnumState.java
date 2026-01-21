package frc.robot.util;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.EnumMap;
import java.util.Map;
import org.littletonrobotics.junction.Logger;

/**
 * A utility class that automatically generates Triggers from any enum. Makes state management dead
 * simple for FRC subsystems.
 *
 * <h2>Basic Usage:</h2>
 *
 * <pre>{@code
 * // 1. Define your states as an enum
 * public enum IntakeState { IDLE, INTAKING, HOLDING, EJECTING }
 *
 * // 2. Create an EnumState (that's it!)
 * private final EnumState<IntakeState> state = new EnumState<>(IntakeState.IDLE);
 *
 * // 3. Use .is() to get a Trigger for any state
 * state.is(IntakeState.HOLDING)  // Returns a Trigger!
 *
 * // 4. Use .set() to change states
 * state.set(IntakeState.INTAKING);
 *
 * // 5. Use .get() to read current state
 * if (state.get() == IntakeState.IDLE) { ... }
 * }</pre>
 *
 * <h2>With Automatic Logging:</h2>
 *
 * <pre>{@code
 * // Add a name and it logs automatically to AdvantageKit!
 * private final EnumState<IntakeState> state =
 *     new EnumState<>("Intake", IntakeState.IDLE);
 * }</pre>
 *
 * <h2>In RobotContainer Bindings:</h2>
 *
 * <pre>{@code
 * // These triggers work just like any other WPILib Trigger
 * intake.state.is(HOLDING)
 *     .and(shooter.state.is(READY))
 *     .onTrue(shootCommand());
 *
 * // Combine states easily
 * intake.state.isAnyOf(IDLE, HOLDING)
 *     .whileTrue(ledCommand());
 * }</pre>
 *
 * @param <E> The enum type representing your states
 */
public class EnumState<E extends Enum<E>> {

  private E currentState;
  private E previousState;
  private final String name;
  private final Class<E> enumClass;
  private final EnumMap<E, Trigger> triggerCache;

  /**
   * Create a new EnumState with the given initial state.
   *
   * <p>Example:
   *
   * <pre>{@code
   * private final EnumState<ShooterState> state = new EnumState<>(ShooterState.IDLE);
   * }</pre>
   *
   * @param initialState The starting state
   */
  public EnumState(E initialState) {
    this(null, initialState);
  }

  /**
   * Create a new EnumState with automatic AdvantageKit logging.
   *
   * <p>The state will be logged to "YourName/State" whenever it changes.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // Logs to "Intake/State" in AdvantageKit
   * private final EnumState<IntakeState> state =
   *     new EnumState<>("Intake", IntakeState.IDLE);
   * }</pre>
   *
   * @param name The name for logging (e.g., "Intake", "Shooter")
   * @param initialState The starting state
   */
  @SuppressWarnings("unchecked")
  public EnumState(String name, E initialState) {
    this.currentState = initialState;
    this.previousState = initialState;
    this.name = name;
    this.enumClass = (Class<E>) initialState.getClass();
    this.triggerCache = new EnumMap<>(enumClass);

    // Pre-create triggers for every enum value
    // This way .is() always returns the same Trigger instance
    for (E value : enumClass.getEnumConstants()) {
      triggerCache.put(value, new Trigger(() -> currentState == value));
    }

    // Log initial state
    log();
  }

  // ==================== CORE METHODS ====================

  /**
   * Get a Trigger that is TRUE when in the specified state.
   *
   * <p>This is the main method you'll use! Example:
   *
   * <pre>{@code
   * // In RobotContainer:
   * intake.state.is(IntakeState.HOLDING)
   *     .onTrue(flashLEDsCommand());
   *
   * // Combine with other triggers:
   * driver.a()
   *     .and(intake.state.is(HOLDING))
   *     .and(shooter.state.is(READY))
   *     .onTrue(shootCommand());
   * }</pre>
   *
   * @param state The state to check for
   * @return A Trigger that is true when currentState equals the given state
   */
  public Trigger is(E state) {
    return triggerCache.get(state);
  }

  /**
   * Change to a new state.
   *
   * <p>If logging is enabled, automatically logs the change to AdvantageKit.
   *
   * <p>Example:
   *
   * <pre>{@code
   * public Command intakeCommand() {
   *     return startEnd(
   *         () -> state.set(IntakeState.INTAKING),
   *         () -> state.set(IntakeState.IDLE)
   *     );
   * }
   * }</pre>
   *
   * @param newState The state to change to
   */
  public void set(E newState) {
    if (currentState != newState) {
      previousState = currentState;
      currentState = newState;
      log();
    }
  }

  /**
   * Get the current state.
   *
   * <p>Example:
   *
   * <pre>{@code
   * if (state.get() == IntakeState.HOLDING) {
   *     // Do something
   * }
   * }</pre>
   *
   * @return The current state
   */
  public E get() {
    return currentState;
  }

  // ==================== CONVENIENCE METHODS ====================

  /**
   * Get a Trigger that is TRUE when in ANY of the specified states.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // True when IDLE or HOLDING (not actively moving)
   * state.isAnyOf(IntakeState.IDLE, IntakeState.HOLDING)
   *     .whileTrue(allowShootingCommand());
   * }</pre>
   *
   * @param states The states to check for (varargs - pass as many as you want)
   * @return A Trigger that is true when in any of the given states
   */
  @SafeVarargs
  public final Trigger isAnyOf(E... states) {
    return new Trigger(
        () -> {
          for (E state : states) {
            if (currentState == state) {
              return true;
            }
          }
          return false;
        });
  }

  /**
   * Get a Trigger that is TRUE when NOT in the specified state.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // True when not idle (doing something)
   * state.isNot(IntakeState.IDLE)
   *     .whileTrue(runMotorCommand());
   * }</pre>
   *
   * @param state The state to check against
   * @return A Trigger that is true when NOT in the given state
   */
  public Trigger isNot(E state) {
    return triggerCache.get(state).negate();
  }

  /**
   * Get a Trigger that is TRUE when NOT in any of the specified states.
   *
   * <p>Example:
   *
   * <pre>{@code
   * // True when not IDLE and not HOLDING
   * state.isNoneOf(IntakeState.IDLE, IntakeState.HOLDING)
   *     .whileTrue(busyIndicatorCommand());
   * }</pre>
   *
   * @param states The states to exclude
   * @return A Trigger that is true when NOT in any of the given states
   */
  @SafeVarargs
  public final Trigger isNoneOf(E... states) {
    return isAnyOf(states).negate();
  }

  /**
   * Get the previous state (what we were in before the last change).
   *
   * <p>Useful for detecting transitions:
   *
   * <pre>{@code
   * if (state.get() == HOLDING && state.getPrevious() == INTAKING) {
   *     // We just finished intaking and now we're holding
   * }
   * }</pre>
   *
   * @return The state we were in before the current state
   */
  public E getPrevious() {
    return previousState;
  }

  // ==================== UTILITY METHODS ====================

  /**
   * Get all available triggers as a map.
   *
   * <p>Useful for debugging or dashboard integration.
   *
   * @return Map from enum value to its corresponding Trigger
   */
  public Map<E, Trigger> triggers() {
    return new EnumMap<>(triggerCache);
  }

  /**
   * Get all possible states for this EnumState.
   *
   * @return Array of all enum constants
   */
  public E[] getAllStates() {
    return enumClass.getEnumConstants();
  }

  /**
   * Get the name used for logging.
   *
   * @return The logging name, or null if logging is disabled
   */
  public String getName() {
    return name;
  }

  /**
   * Manually trigger a log update.
   *
   * <p>Usually not needed since set() logs automatically, but can be called in periodic() if
   * desired.
   */
  public void log() {
    if (name != null) {
      Logger.recordOutput(name + "/State", currentState.name());
    }
  }

  /** Returns the current state name as a string. */
  @Override
  public String toString() {
    return currentState.name();
  }
}

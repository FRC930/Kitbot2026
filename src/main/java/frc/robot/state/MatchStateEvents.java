package frc.robot.state;

import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Interface exposing match phase state as triggers for behaviors to react to.
 * Provides triggers for disabled, autonomous, teleop, and enabled states.
 */
public interface MatchStateEvents {

    /** Trigger when robot is disabled */
    Trigger isDisabled();

    /** Trigger when robot is in autonomous mode */
    Trigger isAutonomous();

    /** Trigger when robot is in teleop mode */
    Trigger isTeleop();

    /** Trigger when robot is enabled (auto OR teleop) */
    Trigger isEnabled();
}

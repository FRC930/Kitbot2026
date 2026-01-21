// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot.util;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.hardware.ParentDevice;
import edu.wpi.first.wpilibj.DriverStation;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility methods for working with Phoenix 6 (CTRE) devices.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * // Retry motor configuration up to 5 times
 * PhoenixUtil.tryUntilOk(5, () ->
 *     motor.getConfigurator().apply(motorConfig),
 *     Optional.of(motor)
 * );
 * }</pre>
 */
public class PhoenixUtil {

  /**
   * Attempts to run the command until no error is produced.
   *
   * @param maxAttempts Maximum number of attempts before giving up
   * @param command The configuration command to run (should return StatusCode)
   * @param device Optional device for better error reporting
   * @return The final StatusCode (OK if successful, error code otherwise)
   */
  public static StatusCode tryUntilOk(
      int maxAttempts, Supplier<StatusCode> command, Optional<ParentDevice> device) {
    StatusCode error = StatusCode.NotFound;
    for (int i = 0; i < maxAttempts; i++) {
      error = command.get();
      if (error.isOK()) break;
      DriverStation.reportWarning(
          String.format(
              "Unable to configure device %s: %s",
              device.isPresent() ? device.get().getDeviceID() : "?", error.toString()),
          true);
    }
    return error;
  }

  /**
   * Attempts to run the command until no error is produced (without device info for errors).
   *
   * @param maxAttempts Maximum number of attempts before giving up
   * @param command The configuration command to run
   * @return The final StatusCode
   */
  public static StatusCode tryUntilOk(int maxAttempts, Supplier<StatusCode> command) {
    return tryUntilOk(maxAttempts, command, Optional.empty());
  }

  /**
   * Get position from controller without explicit casting. Handles cases where the position
   * configuration may not be available yet due to delayed configurations.
   *
   * @param motor The motor to get position from
   * @param defaultPosition Default value if position not available
   * @return The position from the controller, or defaultPosition if not available
   */
  public static double getPositionFromController(ParentDevice motor, double defaultPosition) {
    Map<String, String> map = motor.getAppliedControl().getControlInfo();
    String positionString = map.get("Position");
    double position = defaultPosition;
    if (positionString != null) {
      position = Double.valueOf(positionString);
    }
    return position;
  }
}

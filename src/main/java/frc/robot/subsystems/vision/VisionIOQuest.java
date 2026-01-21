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

package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import gg.questnav.questnav.PoseFrame;
import gg.questnav.questnav.QuestNav;
import java.util.LinkedList;
import java.util.List;

/** IO implementation for real Limelight hardware. */
public class VisionIOQuest implements VisionIO {
  private String cameraName;
  private QuestNav questNav = new QuestNav();
  private final Transform3d ROBOT_TO_QUEST =
      new Transform3d(
          Units.inchesToMeters(0.5),
          Units.inchesToMeters(9.207),
          Units.inchesToMeters(.0),
          new Rotation3d(Rotation2d.fromDegrees(90)));
  private int questDebug = 0;

  /**
   * Creates a new VisionIOLimelight.
   *
   * @param name The configured name of the Limelight.
   * @param rotationSupplier Supplier for the current estimated rotation, used for MegaTag 2.
   */
  public VisionIOQuest(String name) {
    this.cameraName = name;
  }

  @Override
  public void updateInputs(VisionIOInputs inputs) {
    inputs.cameraName = this.cameraName;

    questNav.commandPeriodic(); // Process command responses
    // Update connection status based on whether an update has been seen in the last 250ms
    inputs.connected = questNav.isConnected();
    inputs.isQuestPose = true;

    if (questDebug++ > 50) {
      questDebug = 0;
    }

    // Monitor connection and device status
    if (questNav.isConnected() && questNav.isTracking()) {
      List<PoseObservation> poseObservations = new LinkedList<>();

      // Get latest pose data
      PoseFrame[] newFrames = questNav.getAllUnreadPoseFrames();
      for (PoseFrame questFrame : newFrames) {
        // Use frame.questPose() and frame.dataTimestamp() with pose estimator

        // Get the pose of the Quest
        // Pose3d questPose = questFrame.questPose();
        Pose3d questPose = questFrame.questPose3d();
        // Get timestamp for when the data was sent
        double timestamp = questFrame.dataTimestamp();

        poseObservations.add(
            new PoseObservation(
                cameraName,
                // Timestamp, based on server timestamp of publish and latency
                timestamp,
                // 3D pose estimate
                questPose.transformBy(ROBOT_TO_QUEST.inverse()),
                // Ambiguity, using only the first tag because ambiguity isn't applicable for
                // multitag
                0.0,
                // Tag count
                0,
                // Average tag distance
                0.0,
                // Observation type
                PoseObservationType.QUEST));
      }

      // Save pose observations to inputs object
      inputs.poseObservations = new PoseObservation[poseObservations.size()];
      for (int i = 0; i < poseObservations.size(); i++) {
        inputs.poseObservations[i] = poseObservations.get(i);
      }
    }
  }
}

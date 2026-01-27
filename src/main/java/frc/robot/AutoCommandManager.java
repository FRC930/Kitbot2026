package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.FlippingUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.drive.Drive;
import java.util.Optional;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

public class AutoCommandManager {

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;
  private Drive m_drive;

  public AutoCommandManager(Drive drive) {
    configureNamedCommands(drive);
    m_drive = drive;

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    autoChooser.addOption("PreWrapper", new PathCommandWrapper("Pre"));

    // // Set up SysId routines
    // autoChooser.addOption(
    //     "Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
    // autoChooser.addOption(
    //     "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
    // autoChooser.addOption(
    //     "Drive SysId (Quasistatic Forward)",
    //     drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    // autoChooser.addOption(
    //     "Drive SysId (Quasistatic Reverse)",
    //     drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    // autoChooser.addOption(
    //     "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    // autoChooser.addOption(
    //     "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));
  }

  public Command getAutonomousCommand() {
    // return autoChooser.get();

    Command // command =
        // new InstantCommand(() -> m_drive.setPose(m_drive.getAutoAlignPose()))
        // .andThen(new WaitCommand(1.0))
        // .andThen(autoChooser.get());

        command = autoChooser.get();
    command = getAutoWithCurrentPose();
    return command;
  }

  private void configureNamedCommands(Drive drive) {
    // #region Stows
    NamedCommands.registerCommand("Stow", new InstantCommand());

    // #endregion

    // #region Intakes

    // #endregion

    // #region Prep Scores

    // #endregion

    // #region Confirm Scores

    // #endregion

    // #region StopScore/Backup

    // #endregion

    // Deprecated

    // #endregion
  }

  public Command getAutoWithCurrentPose() {
    Command command = autoChooser.get();
    Command returnCommand = command;

    if (command instanceof PathCommandWrapper) {
      PathCommandWrapper ppAutoCommand = (PathCommandWrapper) command;
      String autoName = ppAutoCommand.m_autoName;
      // TODO detemine if need to prepend (super class with attribute of auto name)
      returnCommand = getToPath(autoName + ".0").andThen(command);
    }
    return returnCommand;
  }

  public Command getToPath(String pathName) {
    Command command;
    try {
      PathPlannerPath path = PathPlannerPath.fromPathFile(pathName);
      Optional<Pose2d> optPath = path.getStartingHolonomicPose();
      PathConstraints constraints =
          new PathConstraints(
              1.0, 1.0, Units.degreesToRadians(540.0 / 2.0), Units.degreesToRadians(720.0 / 2.0));
      // DID NOT work with finally rotation of start of path
      command = AutoBuilder.pathfindThenFollowPath(path, constraints);
      if (optPath.isPresent()) {
        // TODO get constraints from path and starting speed
        // Make sure .0 path and starting velocity is > 0.0
        Pose2d pose;
        if (AutoBuilder.shouldFlip()) {
          pose = FlippingUtil.flipFieldPose(optPath.get());
        } else {
          pose = optPath.get();
        }
        command =
            AutoBuilder.pathfindToPose(
                pose, path.getGlobalConstraints(), path.getIdealStartingState().velocity());
      }
      // spotless:on
      // TODO how to do rest of the path
    } catch (Exception e) {
      command = null;
    }
    return command;
  }
}
// spotless:on

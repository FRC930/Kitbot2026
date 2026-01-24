package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.drive.Drive;
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
}

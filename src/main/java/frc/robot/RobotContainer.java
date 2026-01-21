// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import static frc.robot.subsystems.vision.VisionConstants.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.SignalLogger;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.DriveCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.goals.RobotGoals;
import frc.robot.goals.RobotGoalsBehavior;
import frc.robot.operator.OperatorIntent;
import frc.robot.state.MatchState;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.launcher.LauncherBehavior;
import frc.robot.subsystems.launcher.LauncherIOSim;
import frc.robot.subsystems.launcher.LauncherIOTalonFX;
import frc.robot.subsystems.launcher.LauncherSubsystem;
import frc.robot.subsystems.vision.AprilTagVision;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
import frc.robot.util.GoalBehavior;
import frc.robot.util.SubsystemBehavior;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private final AprilTagVision vision;

  // Subsystems
  private final Drive drive;
  private final double DRIVE_SPEED = 0.55;
  private final double ANGULAR_SPEED = 0.55;

  private final LauncherSubsystem launcher;

  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);
  private final CommandXboxController characterizeController = new CommandXboxController(4);

  // Reactive architecture components
  private final OperatorIntent operatorIntent;
  private final MatchState matchState;
  private final RobotGoals robotGoals;

  private boolean m_teleopInitialized = false;
  private AutoCommandManager autoCommandManager;
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    CANBus canbus = new CANBus("rio");
    switch (Constants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        // ModuleIOTalonFX is intended for modules with TalonFX drive, TalonFX turn, and
        // a CANcoder
        drive =
            new Drive(
                new GyroIOPigeon2(),
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight));

        launcher = new LauncherSubsystem(new LauncherIOTalonFX(19, 11, canbus));

        // The ModuleIOTalonFXS implementation provides an example implementation for
        // TalonFXS controller connected to a CANdi with a PWM encoder. The
        // implementations
        // of ModuleIOTalonFX, ModuleIOTalonFXS, and ModuleIOSpark (from the Spark
        // swerve
        // template) can be freely intermixed to support alternative hardware
        // arrangements.
        // Please see the AdvantageKit template documentation for more information:
        // https://docs.advantagekit.org/getting-started/template-projects/talonfx-swerve-template#custom-module-implementations
        //
        // drive =
        // new Drive(
        // new GyroIOPigeon2(),
        // new ModuleIOTalonFXS(TunerConstants.FrontLeft),
        // new ModuleIOTalonFXS(TunerConstants.FrontRight),
        // new ModuleIOTalonFXS(TunerConstants.BackLeft),
        // new ModuleIOTalonFXS(TunerConstants.BackRight));
        vision =
            new AprilTagVision(
                drive::setPose,
                drive::addVisionMeasurementAutoAlign,
                new VisionIOLimelight(camera0Name, drive::getRotation),
                new VisionIOLimelight(camera1Name, drive::getRotation));
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight));
        vision =
            new AprilTagVision(
                drive::setPose,
                drive::addVisionMeasurementAutoAlign,
                new VisionIOPhotonVisionSim(camera0Name, robotToCamera0, drive::getPose),
                new VisionIOPhotonVisionSim(camera1Name, robotToCamera1, drive::getPose));
        launcher = new LauncherSubsystem(new LauncherIOSim());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        vision =
            new AprilTagVision(
                drive::setPose,
                drive::addVisionMeasurementAutoAlign,
                new VisionIO() {},
                new VisionIO() {});

        launcher = new LauncherSubsystem(new LauncherIOTalonFX(19, 11, canbus));
        break;
    }

    autoCommandManager = new AutoCommandManager(drive);

    // Initialize reactive architecture
    operatorIntent = new OperatorIntent(0);
    matchState = new MatchState();
    robotGoals = new RobotGoals();

    // Create goal behaviors (wires operator intent → robot goals)
    new RobotGoalsBehavior(robotGoals);
    new LauncherBehavior(launcher);

    // TODO (students): Create subsystem behaviors here, e.g.:
    // new LauncherBehavior(launcher);
    // new DriveBehavior(drive);

    // Configure all behaviors
    GoalBehavior.configureAll(operatorIntent);
    SubsystemBehavior.configureAll(robotGoals, matchState, launcher);

    // Configure the button bindings
    configureButtonBindings();
    configureCharacterizationButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Default command, normal field-relative drive
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -controller.getLeftY() * DRIVE_SPEED,
            () -> -controller.getLeftX() * DRIVE_SPEED,
            () -> -controller.getRightX() * ANGULAR_SPEED));

    // // Lock to 0° when A button is held
    // controller
    //     .a()
    //     .whileTrue(
    //         DriveCommands.joystickDriveAtAngle(
    //             drive,
    //             () -> -controller.getLeftY(),
    //             () -> -controller.getLeftX(),
    //             () -> Rotation2d.kZero));

    // Switch to X pattern when X button is pressed
    controller.x().onTrue(Commands.runOnce(drive::stopWithX, drive));

    // Reset gyro to 0° when B button is pressed
    // controller
    //     .b()
    //     .onTrue(
    //         Commands.runOnce(
    //                 () ->
    //                     drive.setPose(
    //                         new Pose2d(drive.getPose().getTranslation(), Rotation2d.kZero)),
    //                 drive)
    //             .ignoringDisable(true));
  }

  public void configureCharacterizationButtonBindings() {
    characterizeController
        .back()
        .and(characterizeController.y())
        .whileTrue(drive.sysIdDynamic(Direction.kForward));
    characterizeController
        .back()
        .and(characterizeController.x())
        .whileTrue(drive.sysIdDynamic(Direction.kReverse));
    characterizeController
        .start()
        .and(characterizeController.y())
        .whileTrue(drive.sysIdQuasistatic(Direction.kForward));
    characterizeController
        .start()
        .and(characterizeController.x())
        .whileTrue(drive.sysIdQuasistatic(Direction.kReverse));

    characterizeController
        .povUp()
        .whileTrue(DriveCommands.wheelRadiusCharacterization(drive))
        .onFalse(DriveCommands.brakeDrive(drive));

    characterizeController
        .a()
        .onTrue(
            new InstantCommand(
                () -> {
                  SignalLogger.setPath("/media/sda1/logs");
                  // SignalLogger.enableAutoLogging(true);
                  SignalLogger.start();
                  System.out.println("Started Logger");
                }));
    characterizeController
        .b()
        .onTrue(
            new InstantCommand(
                () -> {
                  SignalLogger.stop();
                  System.out.println("Stopped Logger");
                }));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    Command autoCommand = autoCommandManager.getAutonomousCommand();
    // turnoff updating odometry based on april tags
    vision.enableUpdateOdometryBasedOnApriltags();
    if (autoCommand != null) {
      // tell vision autonomous path is updated
      vision.updateAutonomous();
    }
    return autoCommand;
  }

  public void teleopInit() {
    if (!this.m_teleopInitialized) {
      vision.updateStartingPosition();
      vision.enableUpdateOdometryBasedOnApriltags();
      m_teleopInitialized = true;
      // TODO uncomment
      //   SignalLogger.setPath("/media/sda1/");
      //   SignalLogger.start();
    }
  }
}

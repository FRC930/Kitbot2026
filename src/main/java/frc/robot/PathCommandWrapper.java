package frc.robot;

import com.pathplanner.lib.commands.PathPlannerAuto;

public class PathCommandWrapper extends PathPlannerAuto {
  public String m_autoName;

  public PathCommandWrapper(String autoName) {
    super(autoName);
    m_autoName = autoName;
  }
}

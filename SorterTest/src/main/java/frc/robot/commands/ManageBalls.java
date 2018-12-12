/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Sorter;
import frc.robot.Robot;
import frc.robot.misc.CameraOutput;

public class ManageBalls extends Command {

    CameraOutput cameraOutput;
  //blank command that the chooser defaults to.

  public ManageBalls(Sorter sorter) { 
    requires(Robot.sorter);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
      cameraOutput = Robot.sorter.getCameraOutput();
      cameraOutput.blueCount
  }

  @Override
  protected boolean isFinished() {
    return true;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
  }
}

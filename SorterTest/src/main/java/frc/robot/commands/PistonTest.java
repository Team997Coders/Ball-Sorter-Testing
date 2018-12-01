/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Sorter;

public class PistonTest extends Command {

    Sorter sorter;
  //blank command that the chooser defaults to.

  public PistonTest(Sorter sorter) { 
    this.sorter = sorter;
    requires(sorter);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
      sorter.testPiston();
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

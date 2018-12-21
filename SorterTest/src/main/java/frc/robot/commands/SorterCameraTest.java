/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.Sorter;

public class SorterCameraTest extends Command {

    Sorter sorter;
    boolean verbose;
  //blank command that the chooser defaults to.

  public SorterCameraTest(Sorter sorter, boolean verbose) { 
    requires(sorter);
    this.sorter = sorter;
    this.verbose = verbose;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void execute() {
    sorter.testCamera();
    System.out.println("Ran testSorter()");
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
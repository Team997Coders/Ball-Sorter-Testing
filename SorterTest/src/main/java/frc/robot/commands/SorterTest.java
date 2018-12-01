/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Robot;
import frc.robot.subsystems.Sorter;
import java.util.concurrent.*;

public class SorterTest extends Command {

  long currentTime;
  long oldTime;
  boolean verbose;
  Sorter sorter;

  public SorterTest(boolean verbose, Sorter sorter) { 
    requires(Robot.sorter);
    this.verbose = verbose;
    this.sorter = sorter;

  }

  @Override
  protected void initialize() {
    sorter.resetSeenValues();
    currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    oldTime = currentTime;
  }

  @Override
  protected void execute() {
    sorter.updateSmartdashboard();
    sorter.setMotor(-0.75);
    currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    if (currentTime > (oldTime + 500)) {
        sorter.testSensor(verbose);
      oldTime = currentTime;
      if (verbose) {
        System.out.println("ran sorter.testSensor @ time " + currentTime + "ms.");
      }
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {
  }

  @Override
  protected void interrupted() {
  }
}
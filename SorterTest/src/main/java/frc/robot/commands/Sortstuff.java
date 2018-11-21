/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.Queue;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.subsystems.Sorter;
import java.util.concurrent.*;
import static java.lang.System.nanoTime;

public class Sortstuff extends Command {

  long currentTime;
  long oldTime;
  boolean verbose;
  Sorter sorter;
  boolean enabled;

  public Sortstuff(boolean verbose, Sorter sorter, boolean enabled) {
    requires(sorter);
    this.sorter = sorter;
    this.verbose = verbose;
    this.enabled = enabled;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    sorter.resetSeenValues();
    currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    oldTime = currentTime;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    if (enabled && currentTime > (oldTime + 500)) {
      sorter.manageQueue();
      oldTime = currentTime;
      if (verbose) {
        System.out.println("ran manageQueue @ time " + currentTime + "ms.");
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}

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
import frc.robot.Robot;

public class Sortstuff extends Command {

  private Queue<String> ballQueue;
  private boolean seenBall;
  private boolean sortingBall;

  public Sortstuff() {
    requires(Robot.sorter);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    seenBall = false;
    sortingBall = false;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if (Robot.ballType.equals("")) {
      seenBall = false;
    } else if (seenBall == false) {
      ballQueue.add(Robot.ballType.getString("error"));
      seenBall = true;
      //TODO figure out a way to correct sync errors. 
    }

    if (Robot.sorter.getBallSensor() && !sortingBall) {
      sortingBall = true;
    } else if (!Robot.sorter.getBallSensor() && sortingBall) {
      sortingBall = false;
      ballQueue.poll();
    }

    if (Robot.sorter.getBallSensor() && ballQueue.peek() == "red") {

        Robot.sorter.extendPiston();
    
    } else if (Robot.sorter.getPistonPosition() == DoubleSolenoid.Value.kForward) { //CCB: Renamed to method for clarity
    
      Robot.sorter.retractPiston();
    
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

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import static java.lang.System.nanoTime;
import java.util.concurrent.*;

public class TimingTest extends Command {

  //blank command that the chooser defaults to.
    long startTime;
    long previousReading;
    long currentTime;
    int cycle;

  public TimingTest() { 

  }

  @Override
  protected void initialize() {
      startTime = TimeUnit.NANOSECONDS.toMillis(nanoTime());
      previousReading = startTime;
      currentTime = TimeUnit.NANOSECONDS.toMillis(nanoTime());
      cycle = 0;
      System.out.println("Timing test initialized @ starttime " + startTime + "ms");
  }

  @Override
  protected void execute() {
      cycle++;
      currentTime = TimeUnit.NANOSECONDS.toMillis(nanoTime());
      if (currentTime > (previousReading + 1000)) {
        previousReading = currentTime;
        System.out.println(cycle);
        cycle = 0;
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

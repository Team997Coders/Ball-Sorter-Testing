/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotMap;
import frc.robot.Robot;

/**
 * Add your docs here.
 */
public class Sorter extends Subsystem {
  private VictorSP motor = new VictorSP(RobotMap.Ports.sorterMotor);
  private DoubleSolenoid piston = new DoubleSolenoid(RobotMap.Ports.sorterPistonOut, RobotMap.Ports.sorterPistonIn);
  private DigitalOutput ballSensor = new DigitalOutput(RobotMap.Ports.ballSensor);
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public void extendPiston() {
    piston.set(Value.kForward);
  }

  public void retractPiston() {
    piston.set(Value.kReverse);
  }

  public Value getPiston() {
    return piston.get();
  }

  public boolean getBallSensor() {
    return ballSensor.get();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
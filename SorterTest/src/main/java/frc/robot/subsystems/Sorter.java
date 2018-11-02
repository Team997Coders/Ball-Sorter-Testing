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
  private VictorSP motor;
  private DoubleSolenoid piston;
  private DigitalOutput ballSensor;
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  // CCB: External dependencies should be "injected" into this class to make it more testable.
  // You can then create a static factory (see NewSorter) on this class if you like so that you do not have to
  // do the new dependent object creation ceremony every time you want an instance of a Sorter.
  public Sorter(VictorSP motor, DoubleSolenoid piston, DigitalOutput ballSensor) {
    this.motor = motor;
    this.piston = piston;
    this.ballSensor = ballSensor;
  }

  //CCB: Sorter factory
  public static Sorter Create() {
    VictorSP motor = new VictorSP(RobotMap.Ports.sorterMotor);
    DoubleSolenoid piston = new DoubleSolenoid(RobotMap.Ports.sorterPistonOut, RobotMap.Ports.sorterPistonIn);
    DigitalOutput ballSensor = new DigitalOutput(RobotMap.Ports.ballSensor);
    return new Sorter(motor, piston, ballSensor);
  }

  public void extendPiston() {
    piston.set(Value.kForward);
  }

  public void retractPiston() {
    piston.set(Value.kReverse);
  }

  // CCB: We are not getting the piston object here, so rename to be more clear.
  public Value getPistonPosition() {
    return piston.get();
  }

  public void ejectBall() {
    // TODO: There is probably some tunable delay that needs to go here
    extendPiston();
    // TODO: There is probably some tunable delay that needs to go here
    retractPiston();
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

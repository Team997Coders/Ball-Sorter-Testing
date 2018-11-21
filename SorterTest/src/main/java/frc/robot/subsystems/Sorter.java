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
import frc.robot.commands.Sortstuff;
import java.util.Queue;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import frc.robot.misc.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;

/**
 * Add your docs here.
 */
public class Sorter extends Subsystem {

  private VictorSP motor = new VictorSP(RobotMap.Ports.sorterMotor);
  private DoubleSolenoid piston = new DoubleSolenoid(RobotMap.Ports.sorterPistonOut, RobotMap.Ports.sorterPistonIn);
  private DigitalOutput ballSensor = new DigitalOutput(RobotMap.Ports.ballSensor);

  public UsbCamera camera0;
  public CvSink imageSink;

  public BlueBall blueBall;
  public RedBall redBall;

  private boolean seenBall; //if true, robot has seen ball with camera, and is waiting for it to leave the fov.
  private boolean sortingBall; //if true and ball is not visible, sets to false and clears the first item in the queue.
  private Queue<String> ballQueue;

  public Sorter() {
     // Init components
     camera0 = new UsbCamera("Camera0", 0); 
     camera0.setResolution(320,240);
 
     imageSink = new CvSink("CV Image Grabber");      //Starts a CV sink to pull camera footage into a MAT image file
     imageSink.setSource(camera0);
 
     blueBall = new BlueBall();
     redBall = new RedBall();
  }

  public String getBall() {
      Color color = getBallColor();
      if (color.ID == Robot.teamColor.ID) {
        System.out.println("Good Ball");
        SmartDashboard.putBoolean("Red Ball?", true);
        return "red";
      } else if (color.ID == Color.NULL.ID) {
        System.out.println("No Ball");
        return "";
      } else {
        System.out.println("Spitting Ball Out");
        SmartDashboard.putBoolean("Red Ball?", false);
        return "blue";
      }
  }

  public void manageQueue() {
    if (getBall().equals("")) {
      seenBall = false;
    } else if (seenBall == false) {
      ballQueue.add(getBall());
      seenBall = true;
      //TODO figure out a way to correct sync errors. 
    }

    if (getBallSensor() && !sortingBall) {
      sortingBall = true;
    } else if (!getBallSensor() && sortingBall) {
      sortingBall = false;
      ballQueue.poll();
    }

    if (getBallSensor() && ballQueue.peek() == "red") {

        extendPiston();
    
    } else if (getPiston() == DoubleSolenoid.Value.kForward) {
    
      retractPiston();
    
    }
  }

  public Color getBallColor() {
    blueBall.process(getImage());
    redBall.process(getImage());

    //boolean blue = false;

    if (blueBall.getOutput().size().area() > 200) { // Pretty useless check but whatever
      System.out.println("Blue ball detected greater than 200 size");
      return Color.blue;
    } else if (redBall.getOutput().size() > 0) {
      if (redBall.getOutput().get(0).size().area() > 200) { // Pretty useless check but whatever
        System.out.println("Red ball detected greater than 200 size");
        return Color.red;
      } else { return Color.NULL; }
    } else { return Color.NULL; }
  }

  public Mat getImage() { Mat image = null; imageSink.grabFrame(image); return image; } // returns MAT image from CvSink

  public void extendPiston() {
    piston.set(Value.kForward);
  }

  public void retractPiston() {
    piston.set(Value.kReverse);
  }

  public Value getPiston() {
    return piston.get();
  }

  public void resetSeenValues() {
    seenBall = false;
    sortingBall = false;
  }

  public boolean getBallSensor() { //is there a ball in front of the piston.
    return ballSensor.get();
  }

  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new Sortstuff(true, this)); //boolean verbose, which sorter to use;
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}

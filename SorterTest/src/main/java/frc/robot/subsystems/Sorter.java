/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.VictorSP;
import frc.robot.RobotMap;
import frc.robot.Robot;
import frc.robot.commands.AutoDefault;
import frc.robot.commands.Sortstuff;
import java.util.Queue;
import java.util.LinkedList;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.MjpegServer;
import frc.robot.misc.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.tables.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;

import java.util.concurrent.TimeUnit;

/**
 * Add your docs here.
 */
public class Sorter extends Subsystem {

  private VictorSP leftMotor = new VictorSP(RobotMap.Ports.leftSorterMotor);
  private VictorSP rightMotor = new VictorSP(RobotMap.Ports.rightSorterMotor);
  private Solenoid piston = new Solenoid(RobotMap.Ports.sorterPiston);
  //private DigitalInput ballSensor = new DigitalInput(RobotMap.Ports.ballSensor);
  private AnalogInput ballSensor = new AnalogInput(RobotMap.Ports.ballSensor);

  public UsbCamera camera0;
  public CvSink imageSink;

  public BlueBall blueBall;
  public RedBall redBall;

  private boolean seenBall = false; //if true, robot has seen ball with camera, and is waiting for it to leave the fov.
  private boolean sortingBall = false; //if true and ball is not visible, sets to false and clears the first item in the queue.
  private Queue<String> ballQueue;
  private long oldTime;
  private long currentTime;
  private int cameraBallCount; //mebe gonna be used to correct sync issues

  private NetworkTable cameraOutputTable;

  public Sorter() {
     // Init components
    ballQueue = new LinkedList<>();
    piston.setPulseDuration(0.05); //a test to see if i can get around some timing issues.

     //camera0 = new UsbCamera("Camera0", 0); 
     //camera0 = CameraServer.getInstance().startAutomaticCapture();
     ///camera0.setResolution(320,240);
     
 
     //imageSink = new CvSink("CV Image Grabber");      //Starts a CV sink to pull camera footage into a MAT image file
     //imageSink.setSource(camera0);
  

     blueBall = new BlueBall();
     redBall = new RedBall();
    
     //NetworkTable.setClientMode();
     //NetworkTable.initialize();

     cameraOutputTable = NetworkTableInstance.getDefault().getTable("SmartDashboard");
  }

  public void testSensor(boolean verbose) {
    currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

    //TODO add an unjamming button that reverses the motors.
    if (getBallSensor() && !sortingBall) {
      sortingBall = true;
      oldTime = currentTime;
    }

    if (currentTime > (oldTime + 25 /*ms*/) && sortingBall) {
      piston.startPulse();
      sortingBall = false;
    }
  }

  public void testCamera() {
    //CameraOutput cameraOutput = getCameraOutput();
    CameraOutput cameraOutput = getNetworkOutput();
    System.out.println("blue:" + cameraOutput.blueCount);
    System.out.println("red:" + cameraOutput.redCount);
  }

  public void manageQueue() {
    CameraOutput cameraOutput = getNetworkOutput();
    if (cameraOutput.blueCount == 0 && cameraOutput.redCount == 0) {
      seenBall = false;
    } else if (seenBall == false) {
      if (cameraOutput.blueCount > 0) {
        ballQueue.add("blue");
        System.out.println("Found blue ball");
        cameraBallCount += cameraOutput.blueCount;
      } else if (cameraOutput.redCount > 0) {
        ballQueue.add("red");
        System.out.println("Found red ball");
        cameraBallCount += cameraOutput.redCount;
      }
      seenBall = true;
      //TODO figure out a way to correct sync errors. (added a ballCount?)
      //I think i broke it more by using the blob count.  
    }
    //TODO add an unjamming button that reverses the motors.
    

  }

  public void managePiston() {
    currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
    if (ballQueue.size() > 0) {
      if (getBallSensor() && !sortingBall) {
        String ballColor = ballQueue.poll().toString();
        if (!Robot.allianceColor.equalsIgnoreCase(ballColor)) {
          System.out.println("Beam break sensor found bad ball: " + ballColor);
          sortingBall = true;
          oldTime = currentTime;
        }
      } 
    }

    if (currentTime > (oldTime + 35 /*ms*/) && sortingBall) {
      piston.startPulse();
      sortingBall = false;
    }

  }

  public CameraOutput getCameraOutput() {

    Mat img = getImage();

    if (img == null) {
      return new CameraOutput(0, 0);
    }

    blueBall.process(img);
    redBall.process(img);

    CameraOutput output = new CameraOutput(redBall.getOutput().size(), blueBall.getOutput().rows());
    return output;
    
    //These methods are different because I picked the sketchy camera code to use. ^^^

    //This stuff down here is deprecated in theory, but i doubt the new stuff works either. vvv


    //boolean blue = false;

    /*if (blueBall.getOutput().size().area() > 200) { // Pretty useless check but whatever
      System.out.println("Blue ball detected greater than 200 size");
      return Color.blue;

    } else if (redBall.getOutput().size() > 0) {
      if (redBall.getOutput().get(0).size().area() > 200) { // Pretty useless check but whatever
        System.out.println("Red ball detected greater than 200 size");
        return Color.red;

      } else { return Color.NULL; }
    } else { return Color.NULL; }*/
  }

  public Mat getImage() { 
    Mat image = null;
    try {
      imageSink.grabFrame(image, 0.5);
    } catch (Exception e) {
      System.out.println("No Image");
      e.printStackTrace();
    }
    return image;
  } // returns MAT image from CvSink

  public void extendPiston() {
    piston.set(true);
  }

  public void retractPiston() {
    piston.set(false);
  }

  public boolean getPiston() {
    return piston.get();
  }

  public void resetSeenValues() {
    seenBall = false;
    sortingBall = false;
    piston.set(false);
  }

  public boolean getBallSensor() { //is there a ball in front of the piston.
    return ballSensor.getVoltage() < 0.1;
      //return !ballSensor.get();
  }

  public void setMotor(double speed) {
    leftMotor.set(-speed);
    rightMotor.set(-speed);
  }

  public void stopMotors() {
    leftMotor.set(0);
    rightMotor.set(0);
  }

  /*
   *  Use this function to get ball count from network tables
   * 
   *  @return CameraOutput
   */
  public CameraOutput getNetworkOutput() {
    double red = cameraOutputTable.getEntry("RedBallCount").getDouble(0);
    double blue = cameraOutputTable.getEntry("BlueBallCount").getDouble(0);

    return new CameraOutput((int)red, (int)blue);
  }

  public void updateSmartdashboard() {
    SmartDashboard.putBoolean("Piston Extended:", getPiston());
    SmartDashboard.putNumber("Ball sensor voltage", ballSensor.getVoltage());
    SmartDashboard.putBoolean("Ball in BB sensor", getBallSensor());
    SmartDashboard.putBoolean("seen Ball", sortingBall);
    //SmartDashboard.putStringArray("ball queue", queueToStringArray(ballQueue));
  }

  public String[] queueToStringArray(Queue<String> queue) {
    String[] array = new String[queue.size()];
    Object[] queueArray = queue.toArray();
    for (int i = 0; i < queue.size(); i++) {
      array[i] = (String)queueArray[i];
    }
    return array;
  }

  @Override
  public void initDefaultCommand() {
    //setDefaultCommand(new Sortstuff(true, this)); //boolean verbose, which sorter to use;
    
    
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  //  |
  // ^o^
}

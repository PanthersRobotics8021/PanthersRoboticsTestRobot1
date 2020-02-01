/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.io.Console;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class GTADrive extends Command {
  public GTADrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    
  }

  boolean reverse = false;

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //variables
    double joyX = Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_X);
    double joyY = Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_Y);
    double joyZ = Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_Z);
    double throttle = 1 - Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_SLIDE);
    boolean thumbButton = Robot.m_oi.GetButton(RobotMap.THUMB_BUTTON);


    //motor variables
    double turnValue = 0;
    double lMotors = joyY; 
    double rMotors = joyY;


    //reverse system
    if (thumbButton) {
      reverse = !reverse;
    }

    if (reverse) {
      lMotors = -lMotors;
      rMotors = -rMotors;
    }


    //turn system
    if (joyX > RobotMap.TURN_THRESHOLD || joyX < -RobotMap.TURN_THRESHOLD) {
      turnValue += joyX;
    }

    if (joyZ > RobotMap.TURN_THRESHOLD || joyZ < -RobotMap.TURN_THRESHOLD) {
      turnValue += joyZ;
    }


    //turning values
    if (turnValue < 0 || turnValue > 0) {
      turnValue *= .6;
      lMotors *= RobotMap.MOTOR_SPEED_FACTOR;
      rMotors *= RobotMap.MOTOR_SPEED_FACTOR;
      lMotors -= turnValue * RobotMap.MOTOR_SPEED_FACTOR;
      rMotors += turnValue * RobotMap.MOTOR_SPEED_FACTOR;
    }
    else {
      lMotors *= RobotMap.MOTOR_SPEED_FACTOR;
      rMotors *= RobotMap.MOTOR_SPEED_FACTOR;
    }

    //final command
    Robot.driveTrain.setLeftMotors(lMotors * throttle);
    Robot.driveTrain.setRightMotors(rMotors * throttle);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveTrain.setLeftMotors(0);
    Robot.driveTrain.setRightMotors(0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    this.end();
  }
}

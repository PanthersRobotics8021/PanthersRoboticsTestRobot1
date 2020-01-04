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

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //variables
    double joyX = Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_X);
    double joyY = Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_Y);
    double joyZ = Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_Z);
    double joySlider = 1 - Robot.m_oi.GetDriverRawAxis(RobotMap.JOY_SLIDE);
    boolean thumbButton = Robot.m_oi.GetButton(RobotMap.THUMB_BUTTON);

    double turnValue;
    double lMotors = joyY; 
    double rMotors = joyY;

    //joystick x turning threshold
    if (joyX > 1 + RobotMap.TURN_THRESHOLD || joyX < 1 - RobotMap.TURN_THRESHOLD) {
      turnValue = joyZ + joyX;
    }
    else {
      turnValue = joyZ;
    }
    

    //turning values
    if (turnValue < 0 || turnValue > 0) {
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
    Robot.driveTrain.setLeftMotors(lMotors * joySlider);
    Robot.driveTrain.setRightMotors(rMotors * joySlider);
    Robot.driveTrain.invertMotors(thumbButton);
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

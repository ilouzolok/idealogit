package com.idealo.robot.model;

import org.springframework.ui.Model;

/**
 * Holds the robot data that is set upon processing of the script,
 * and that consists of the updated robot position and the direction it
 * looks into. an object of this type is added to the {@link Model} to be accessible
 * from the View.
 * 
 * @author IgorPelvain
 */
public class RobotModel {

	private int[]position;
	private String direction;
	
	//Getters and Setters
	
	public RobotModel(int[]coordinates, String direction) {
		this.position = coordinates;
		this.direction = direction;
	}
	
	public int[] getPosition() {
		return position;
	}
	public void setPosition(int[] coordinates) {
		this.position = coordinates;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public int getRow() {
		if (position != null) {
			return position[0];
		}
		return -1;
	}
	
	public int getCol() {
		if (position != null) {
			return position[1];
		}
		return -1;
	}
}

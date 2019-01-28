package com.idealo.robot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.idealo.robot.model.RobotModel;

/**
 * Defines utility methods for processing the robot script
 * and updating the model according to a movement logic
 * 
 * @author IgorPelvain
 */
@Service
public class RobotService {

	private RobotModel robotModel = null;
	private int[] position = new int[2];
	private String direction = null;
	private int steps;
	private static final int UPPER_LIMIT = 4;
	private static final int LOWER_LIMIT = 0;
	
	
	/**
	 * Reads a script provided as a {@link String} and returns a list
	 * of move commands to be executed by the robot
	 * @param script a {@link String} representation of the robot move sequence
	 * @return a {@link List} of all commands in the script
	 */
	public List<String> parseRobotScript(String script){
		List<String>commands = new ArrayList<String>();
		if(script != null && !script.isEmpty()) {
			//Convert the script text as a series of command lines that can be interpreted each
			String[]lines = script.split("\n");
			//Get Rid of the comments in each script line, if any!
			for(String line:lines) {
				if(!line.isEmpty()) {
					if(line.contains("//")) {
						line = line.split("//")[0]; 
					}
					commands.add(line.trim());
				}
			}
		}
		return commands;
	}

	/**
	 * Defines a movement logic to update the robot position by interpreting a
	 * set of commands provided in the script.
	 * @param script a block of text submitted from the front-end UI to be processed
	 * @throws NumberFormatException if the position of the robot, or the number of steps cannot be
	 * parsed properly
	 */
	public RobotModel processRobotScript(String script)throws NumberFormatException {
		List<String>commandLines = parseRobotScript(script);

		if(!commandLines.isEmpty()) {
			commandLines.forEach(line ->{
			String[]parts = line.split("\\s+");
			//Each line starts with a move command.
			String commandName = parts[0];
			if(commandName.equalsIgnoreCase("POSITION")) {
				//Extract the initial position in the command line
				position[0] = Integer.parseInt(parts[1]);
				position[1] = Integer.parseInt(parts[2]);
				//Extract the initial direction from the command line
				direction = parts[3];
				if(robotModel == null) {
					robotModel = new RobotModel(position, direction);
				}
				else {
					robotModel.setPosition(position);
					robotModel.setDirection(direction);
				}
			}
			//Move the robot in its current direction
			else if (commandName.equalsIgnoreCase("FORWARD")) {
				//Get the number of steps specified in the command line
				steps = Integer.parseInt(parts[1]);
				int distance = 0;
				if(robotModel != null) {
					position = robotModel.getPosition();
					if(robotModel.getDirection().equalsIgnoreCase("EAST")) {
						distance = position[1] += steps;
						if(distance > UPPER_LIMIT) {
							position[1] = UPPER_LIMIT;
						}
						else {
							position[1] = distance;
						}
					}
					else {
						//'Minus' indicates a move in the opposite direction
						distance = position[1] -= steps;
						if(distance < LOWER_LIMIT) {
							position[1] = LOWER_LIMIT;
						}
						else {
							position[1] = distance;
						}
					}
					robotModel.setPosition(position);
				}
				else {
					//If the robotModel is null, that implies it has not moved yet from its initial position
					position[1] = steps;
					direction = "EAST";
					robotModel = new RobotModel(position, direction);
				}
			}
			else if (commandName.equalsIgnoreCase("WAIT")) {
				//Do Nothing! Empty blocks should be avoided!
			}
			else if(commandName.equalsIgnoreCase("TURNAROUND")) {
				if(robotModel != null) {
					if(robotModel.getDirection().equalsIgnoreCase("EAST")) {
						robotModel.setDirection("WEST");
					}
					else {
						robotModel.setDirection("EAST");
					}
				}
			}
			else {
				if(robotModel != null) {
					if(!robotModel.getDirection().equalsIgnoreCase("EAST")) {
						robotModel.setDirection("EAST");
					}
				}
			}
			
			});
			
		}
		return robotModel;
	}
}

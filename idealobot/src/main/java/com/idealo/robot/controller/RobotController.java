package com.idealo.robot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.idealo.robot.model.RobotModel;
import com.idealo.robot.service.RobotService;

/**
 * Defines the back-end of the application where all requests are directed and 
 * handled by end points.
 * @author Igor Pelvain
 */
@Controller
@RequestMapping("/idealo/robot")
public class RobotController {

	@Autowired
	RobotService robotService;
	
	/**
	 * Handles the POST request holding the script to be processed, and returns a fragment of
	 * HTML containing the updated position of the robot, upon success
	 * @param script a block of text commands to be processed
	 * @param model a object that holds the results of the processing and that is available to create the HTML view
	 * @return a piece of HTML holding the processing results
	 */
	@PostMapping("/run-script")
	public String moveRobot(@RequestBody String script, Model model) {
		try {
			RobotModel robotModel = robotService.processRobotScript(script);
			if(robotModel != null) {
				model.addAttribute("robotModel", robotModel);
			}
		}catch(NumberFormatException nException) {
			nException.printStackTrace();
		}
		return "robotView:: results";
	}
}

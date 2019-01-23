package com.idealo.robot.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import com.idealo.robot.model.RobotModel;
import com.idealo.robot.service.RobotService;

@RunWith(SpringRunner.class)
@WebMvcTest(RobotController.class)
public class RobotControllerTest {

	@MockBean
	private RobotService robotServiceMock;
	@MockBean
	private Model modelMock;
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testMoveRobot() throws Exception  {
		String robotScript = "POSITION 3 1 EAST \n";
		int[] position = {3, 1};
		String direction = "EAST";
		String expectedResult = "3-1:EAST";
		RobotModel robotModelMock = new RobotModel(position, direction);
		
		given(robotServiceMock.processRobotScript(robotScript)).willReturn(robotModelMock);
		
		mockMvc.perform(post("/idealo/robot/run-script")
				.content(robotScript))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString(expectedResult)));
	}
	
}

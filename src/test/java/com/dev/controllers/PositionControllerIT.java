package com.dev.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dev.util.TokenUtil;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PositionControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String employeeUsername, employeeCredentials, ceoUsername, ceoCredentials;
	private String employeeToken, ceoToken, invalidToken;
	
	@BeforeEach
	void setUp() throws Exception {
		employeeUsername = "caue@gmail.com";
		employeeCredentials = "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS";
		ceoUsername = "ricardison@gmail.com";
		ceoCredentials = "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS";
		
		employeeToken = tokenUtil.obtainAccessToken(mockMvc, employeeUsername, employeeCredentials);
		ceoToken = tokenUtil.obtainAccessToken(mockMvc, ceoUsername, ceoCredentials);
		invalidToken = ceoToken + "credentials"; // Simulating invalid token
		
	}
	
	@Test
	public void findAllShouldReturnListOfPositionDTO() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/positions")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());	
		result.andExpect(jsonPath("$.[0].id").value(1L));
		result.andExpect(jsonPath("$.[0].position").value("Software developer"));
		result.andExpect(jsonPath("$.[0].salary").value(5600.0));
	}
}

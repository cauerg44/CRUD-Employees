package com.dev.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.dev.dto.PositionDTO;
import com.dev.entities.Position;
import com.dev.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PositionControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String employeeUsername, employeeCredentials, ceoUsername, ceoCredentials;
	private String employeeToken, ceoToken, invalidToken;
	
	private Position position;
	private PositionDTO positionDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		employeeUsername = "caue@gmail.com";
		employeeCredentials = "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS";
		ceoUsername = "ricardison@gmail.com";
		ceoCredentials = "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS";
		
		employeeToken = tokenUtil.obtainAccessToken(mockMvc, employeeUsername, employeeCredentials);
		ceoToken = tokenUtil.obtainAccessToken(mockMvc, ceoUsername, ceoCredentials);
		invalidToken = ceoToken + "credentials"; // Simulating invalid token
		
		position = new Position(null, "Tech leader", 8600.00);
		positionDTO = new PositionDTO(position);
	}
	
	@Test // TEST FAILING - EXPECTED 200 BUT WAS 400
	public void findAllShouldReturnListOfPositionDTO() throws Exception {
		
		ResultActions result = 
				mockMvc.perform(get("/positions")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$[0].id").value(1));
		result.andExpect(jsonPath("$[0].position").value("Software developer"));
		result.andExpect(jsonPath("$[0].salary").value(5600.0));
		result.andExpect(jsonPath("$[1].id").value(2));
		result.andExpect(jsonPath("$[1].position").value("Data analyst"));
		result.andExpect(jsonPath("$[1].salary").value(5600.0));
	}
	
	@Test // TEST FAILING - EXPECTED 200 BUT WAS 400
	public void insertShouldReturnPositionDTOCreatedWhenLoggedAsCEO() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(positionDTO);
		
		ResultActions result = 
						mockMvc.perform(post("/positions")
								.header("Authorization", "Bearer " + ceoToken)
								.content(jsonBody)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(5L));
		result.andExpect(jsonPath("$.position").value("Tech Leader"));
		result.andExpect(jsonPath("$.salary").value(8600.00));
	}
}

package com.dev.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dev.dto.PositionDTO;
import com.dev.entities.Position;
import com.dev.factories.PositionFactory;
import com.dev.repositories.PositionRepository;

@ExtendWith(SpringExtension.class)
public class PositionServiceTests {

	@InjectMocks
	private PositionService service;
	
	@Mock
	private PositionRepository repository;
	
	private PositionDTO positionDTO;
	
	private Position position;
	private List<Position> list;
	
	@BeforeEach
	void setUp() throws Exception {
		position = PositionFactory.createPosition();
		positionDTO = new PositionDTO(position);
		
		list = new ArrayList<>();
		list.add(position);
		
		Mockito.when(repository.findAll()).thenReturn(list);
		
		Mockito.when(repository.save(any())).thenReturn(position);
	}
	
	@Test
	public void findAllShouldReturnListPositionDTO() {
		
		List<PositionDTO> result = service.findAll();
		
		Assertions.assertEquals(result.size(), 1);
		Assertions.assertEquals(result.get(0).getId(), position.getId());
		Assertions.assertEquals(result.get(0).getPosition(), position.getPosition());
		Assertions.assertEquals(result.get(0).getSalary(), position.getSalary());
	}
	
	@Test
	public void insertShouldReturnNewPositionDTO() {
		
		PositionDTO newPosition = service.insert(positionDTO);
	
		Assertions.assertNotNull(newPosition);
		Assertions.assertEquals(newPosition.getId(), position.getId());
	}
	
}

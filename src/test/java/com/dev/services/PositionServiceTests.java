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
import com.dev.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class PositionServiceTests {

	@InjectMocks
	private PositionService service;
	
	@Mock
	private PositionRepository repository;
	
	private Long existingPositionId, nonExistingPositionId;
	private String positionName;
	private PositionDTO positionDTO;
	private Position position;
	private List<Position> list;
	
	@BeforeEach
	void setUp() throws Exception {
		existingPositionId = 1L;
		nonExistingPositionId = 2L;
		
		positionName = "UX Designer";
		
		position = PositionFactory.createPosition(positionName);
		positionDTO = new PositionDTO(position);
		
		list = new ArrayList<>();
		list.add(position);
		
		Mockito.when(repository.findAll()).thenReturn(list);
		
		Mockito.when(repository.save(any())).thenReturn(position);
		
		Mockito.when(repository.getReferenceById(existingPositionId)).thenReturn(position);
		Mockito.when(repository.getReferenceById(nonExistingPositionId)).thenThrow(EntityNotFoundException.class);
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
		
		PositionDTO result = service.insert(positionDTO);
	
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), position.getId());
		Assertions.assertEquals(result.getPosition(), position.getPosition());
		Assertions.assertEquals(result.getSalary(), position.getSalary());
	}
	
	@Test
	public void updateShouldReturnNewPositionDTOWhenIdExists() {
		
		PositionDTO result = service.update(existingPositionId, positionDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingPositionId);
		Assertions.assertEquals(result.getPosition(), positionDTO.getPosition());
		Assertions.assertEquals(result.getSalary(), positionDTO.getSalary());
	}
	
	@Test
	public void updateShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingPositionId, positionDTO);
		});
	}
}

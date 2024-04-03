package com.dev.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dev.dto.EmployeeDTO;
import com.dev.entities.Employee;
import com.dev.factories.EmployeeFactory;
import com.dev.repositories.EmployeeRepository;
import com.dev.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTests {

	@InjectMocks
	private EmployeeService service;
	
	@Mock
	private EmployeeRepository repository;
	
	private Long existingEmployeeId, nonExistingEmployeeId;
	private String employeeName;
	private Employee employee;
	private EmployeeDTO employeeDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		existingEmployeeId = 1L;
		nonExistingEmployeeId = 2L;
		
		employeeName = "Cauê Garcia";
		
		employee = EmployeeFactory.createEmployee(employeeName);
		employeeDTO = new EmployeeDTO(employee);
		
		Mockito.when(repository.findById(existingEmployeeId)).thenReturn(Optional.of(employee));
		Mockito.when(repository.findById(nonExistingEmployeeId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void findByIdShouldReturnEmployeeDTOWhenIdExists() {
		
		EmployeeDTO result = service.findById(existingEmployeeId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingEmployeeId);
		Assertions.assertEquals(result.getName(), employee.getName());
		Assertions.assertEquals(result.getEmail(), employee.getEmail());
		Assertions.assertEquals(result.getPosition(), employee.getPosition());
	}
	
	@Test
	public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotException() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingEmployeeId);
		});
	}
	
}

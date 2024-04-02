package com.dev.services;

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

import com.dev.dto.DepartmentDTO;
import com.dev.entities.Department;
import com.dev.factories.DepartmentFactory;
import com.dev.repositories.DepartmentRepository;

@ExtendWith(SpringExtension.class)
public class DepartmentServiceTests {

	@InjectMocks
	private DepartmentService service;
	
	@Mock
	private DepartmentRepository repository;
	
	private Department department;
	private List<Department> list;
	
	@BeforeEach
	void setUp() throws Exception {
		department = DepartmentFactory.createDepartment();
		
		list = new ArrayList<>();
		list.add(department);
		
		Mockito.when(repository.findAll()).thenReturn(list);
	}
	
	@Test
	public void findAllShouldReturnListDepartmentDTO() {
		
		List<DepartmentDTO> result = service.findAll();
		
		Assertions.assertEquals(result.size(), 1);
		Assertions.assertEquals(result.get(0).getId(), department.getId());
		Assertions.assertEquals(result.get(0).getName(), department.getName());
	}
	
	
	
	
}

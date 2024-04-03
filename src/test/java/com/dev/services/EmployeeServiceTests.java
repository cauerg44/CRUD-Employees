package com.dev.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dev.dto.EmployeeDTO;
import com.dev.entities.Employee;
import com.dev.factories.EmployeeDetailsFactory;
import com.dev.factories.EmployeeFactory;
import com.dev.projections.EmployeeDetailsProjection;
import com.dev.repositories.EmployeeRepository;
import com.dev.services.exceptions.DatabaseException;
import com.dev.services.exceptions.ResourceNotFoundException;
import com.dev.util.CustomUserUtil;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTests {

	@InjectMocks
	private EmployeeService service;
	
	@Mock
	private EmployeeRepository repository;
	
	@Mock
	private CustomUserUtil userUtil;
	
	private String existingEmployeeEmail, nonExistingEmployeeEmail;
	private List<EmployeeDetailsProjection> employeeDetails;
	
	private Long existingEmployeeId, nonExistingEmployeeId, dependentEmployeeId;
	private Employee newEmployee;
	private String employeeName;
	private Employee employee;
	private EmployeeDTO employeeDTO;
	private PageImpl<Employee> page;
	
	@BeforeEach
	void setUp() throws Exception {
		existingEmployeeEmail = "caue@gmail.com";
		nonExistingEmployeeEmail = "user@gmail.com";
		
		newEmployee = EmployeeFactory.createCustomEmployee(1L, existingEmployeeEmail);
		employeeDetails = EmployeeDetailsFactory.createCustomCEO(existingEmployeeEmail);
		
		
		existingEmployeeId = 1L;
		nonExistingEmployeeId = 2L;
		dependentEmployeeId = 3L;
		
		employeeName = "Cauê Garcia";
		
		employee = EmployeeFactory.createEmployee(employeeName);
		employeeDTO = new EmployeeDTO(employee);
		page = new PageImpl<>(List.of(employee));
		
		Mockito.when(repository.findById(existingEmployeeId)).thenReturn(Optional.of(employee));
		Mockito.when(repository.findById(nonExistingEmployeeId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);
		
		Mockito.when(repository.save(any())).thenReturn(employee);
		
		Mockito.when(repository.getReferenceById(existingEmployeeId)).thenReturn(employee);
		Mockito.when(repository.getReferenceById(nonExistingEmployeeId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.existsById(existingEmployeeId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingEmployeeId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentEmployeeId)).thenReturn(true);
		
		Mockito.doNothing().when(repository).deleteById(existingEmployeeId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentEmployeeId);
		
		Mockito.when(repository.searchEmployeeAndRolesByEmail(existingEmployeeEmail)).thenReturn(employeeDetails);
		Mockito.when(repository.searchEmployeeAndRolesByEmail(nonExistingEmployeeEmail)).thenReturn(new ArrayList<>());
	
		Mockito.when(repository.findByEmail(existingEmployeeEmail)).thenAnswer(invocation -> Optional.of(newEmployee));
		Mockito.when(repository.findByEmail(nonExistingEmployeeEmail)).thenAnswer(invocation -> Optional.empty());
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
	
	@Test
	public void findAllShouldReturnPagedEmployeeDTO() {
		
		Pageable pageable = PageRequest.of(0, 12);
		
		Page<EmployeeDTO> result = service.findAll(employeeName, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getName(), employee.getName());		
	}
	
	@Test
	public void insertShouldReturnEmployeeDTO() {
		
		EmployeeDTO result = service.insert(employeeDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), employee.getId());
	}
	
	@Test
	public void updateShouldReturnEmployeeDTOWhenIdExists() {
		
		EmployeeDTO result = service.update(existingEmployeeId, employeeDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingEmployeeId);
		Assertions.assertEquals(result.getName(), employeeDTO.getName());
	}
	
	@Test
	public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingEmployeeId, employeeDTO);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingEmployeeId);
		});
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingEmployeeId);
		});
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentEmployeeId);
		});
	}
	
	@Test
	public void loadUserByUsernameShouldReturnEmployeeDetailsWhenUserExists() {
		
		UserDetails result = service.loadUserByUsername(existingEmployeeEmail);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingEmployeeEmail);
	}
	
	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenEmployeeDoesNotExist() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingEmployeeEmail);
		});
	}
	
	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingEmployeeEmail);
		
		Employee result = service.authenticated();
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), existingEmployeeEmail);
	}
	
	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
		
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}
	
	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenEmployeeUserDoesNotExist() {
		
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
	}
	
	@Test
	public void getMeShouldReturnEmployeeDTOWhenUserAuthenticated() {
	
		EmployeeService spyUserService = Mockito.spy(service);
		Mockito.doReturn(newEmployee).when(spyUserService).authenticated();
		
		EmployeeDTO result = spyUserService.getMe();
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getEmail(), existingEmployeeEmail);		
	}
	
	@Test
	public void getMeShouldThrowUsernameNotFoundExceptionWhenEmployeeIsNotAuthenticated() {
		
		EmployeeService spyUserService = Mockito.spy(service);
		Mockito.doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			@SuppressWarnings("unused")
			EmployeeDTO result = spyUserService.getMe();
		});
	}
}

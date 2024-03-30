package com.dev.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dto.EmployeeDTO;
import com.dev.entities.Department;
import com.dev.entities.Employee;
import com.dev.entities.Position;
import com.dev.repositories.DepartmentRepository;
import com.dev.repositories.EmployeeRepository;
import com.dev.repositories.PositionRepository;
import com.dev.services.exceptions.DatabaseException;
import com.dev.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Transactional(readOnly = true)
	public List<EmployeeDTO> findAll() {
		List<Employee> list = employeeRepository.findAll();
		return list.stream().map(x -> new EmployeeDTO(x)).toList();
	}

	@Transactional
	public EmployeeDTO insert(EmployeeDTO dto) {
		Employee obj = new Employee();
		obj.setName(dto.getName());
		obj.setEmail(dto.getEmail());
		obj.setBirthDate(dto.getBirthDate());
		obj.setCredentials(dto.getCredentials());
		
		if (dto.getPosition() != null) {
			Position position = positionRepository.findById(dto.getPosition().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
			obj.setPosition(position);
			
		}
		if (dto.getEmployeeDepartment() != null) {
			Set<Department> departments = dto.getEmployeeDepartment().stream()
					.map(departmentDto -> departmentRepository.findById(departmentDto.getId())
							.orElseThrow(() -> new ResourceNotFoundException("Departamento não encontrado")))
					.collect(Collectors.toSet());
			obj.getDepartments().addAll(departments);
		}
		
		obj = employeeRepository.save(obj);
		return new EmployeeDTO(obj);
	}

	@Transactional
	public EmployeeDTO update(Long id, EmployeeDTO dto) {
	    try {
	        Employee obj = employeeRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
	        obj.setName(dto.getName());
	        obj.setEmail(dto.getEmail());
	        obj.setBirthDate(dto.getBirthDate());
	        obj.setCredentials(dto.getCredentials());

	        if (dto.getPosition() != null) {
	            Position position = positionRepository.findById(dto.getPosition().getId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Position not found."));
	            obj.setPosition(position);
	        }

	        if (dto.getEmployeeDepartment() != null) {
	            Set<Department> departments = dto.getEmployeeDepartment().stream()
	                    .map(departmentDto -> departmentRepository.findById(departmentDto.getId())
	                            .orElseThrow(() -> new ResourceNotFoundException(
	                                    "Department not found with id: " + departmentDto.getId())))
	                    .collect(Collectors.toSet());
	            obj.getDepartments().clear();
	            obj.getDepartments().addAll(departments);
	        }

	        obj = employeeRepository.save(obj);
	        return new EmployeeDTO(obj);
	    } catch (EntityNotFoundException e) {
	        throw new ResourceNotFoundException("Employee not found with id: " + id);
	    }
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!employeeRepository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found.");
		}
		try {
			employeeRepository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Fail in integrity violation;");
		}
	}
	
}

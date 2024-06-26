package com.dev.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dto.DepartmentDTO;
import com.dev.dto.EmployeeDTO;
import com.dev.entities.Department;
import com.dev.entities.Employee;
import com.dev.entities.Position;
import com.dev.entities.Role;
import com.dev.projections.EmployeeDetailsProjection;
import com.dev.repositories.DepartmentRepository;
import com.dev.repositories.EmployeeRepository;
import com.dev.repositories.PositionRepository;
import com.dev.services.exceptions.DatabaseException;
import com.dev.services.exceptions.ResourceNotFoundException;
import com.dev.util.CustomUserUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private CustomUserUtil customUserUtil;

	@Transactional(readOnly = true)
	public EmployeeDTO findById(Long id) {
		Employee emp = employeeRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Resource not found."));
		return new EmployeeDTO(emp);
	}
	
	@Transactional(readOnly = true)
	public Page<EmployeeDTO> findAll(String name, Pageable pageable) {
		Page<Employee> list = employeeRepository.searchByName(name, pageable);
		return list.map(x -> new EmployeeDTO(x));
	}

	@Transactional
	public EmployeeDTO insert(EmployeeDTO dto) {
		Employee obj = new Employee();
		copyDtoToEntity(dto, obj);	
		obj = employeeRepository.save(obj);
		return new EmployeeDTO(obj);
	}

	@Transactional
	public EmployeeDTO update(Long id, EmployeeDTO dto) {
	    try {
	        Employee obj = employeeRepository.getReferenceById(id);
	        copyDtoToEntity(dto, obj);	
	        obj = employeeRepository.save(obj);
	        return new EmployeeDTO(obj);
	    } 
	    catch (EntityNotFoundException e) {
	        throw new ResourceNotFoundException("Employee not found.");
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

	private void copyDtoToEntity(EmployeeDTO dto, Employee entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setBirthDate(dto.getBirthDate());
		entity.setCredentials(dto.getCredentials());
		
		if (dto.getPosition() != null) {
			Position pos = positionRepository.findById(dto.getPosition().getId()).orElseThrow(
					() -> new ResourceNotFoundException("Resource not found."));
	        Position position = new Position();
	        position.setId(pos.getId());
			position.setPosition(pos.getPosition());
			position.setSalary(pos.getSalary());
	        entity.setPosition(position);
	    }
		
		entity.getDepartments().clear();
		for (DepartmentDTO depDTO : dto.getDepartment()) {
			Department department = new Department();
			department.setId(depDTO.getId());
			department.setName(depDTO.getName());
			entity.getDepartments().add(department);
		}
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<EmployeeDetailsProjection> result = employeeRepository.searchEmployeeAndRolesByEmail(username);
		if (result.isEmpty()) {
			throw new UsernameNotFoundException("User not found.");
		}
		
		Employee emp = new Employee();
		emp.setEmail(username);
		emp.setCredentials(result.get(0).getCredentials());
		for (EmployeeDetailsProjection projection : result) {
			emp.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return emp;
	}
	
	protected Employee authenticated() {
		try {
			String username = customUserUtil.getLoggedUsername();
			return employeeRepository.findByEmail(username).get();
		}
		catch(Exception e) {
			throw new UsernameNotFoundException("Invalid employee.");		}
	}
	
	@Transactional(readOnly = true)
	public EmployeeDTO getMe() {
		Employee emp = authenticated();
		return new EmployeeDTO(emp);
	}
}

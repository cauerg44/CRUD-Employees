package com.dev.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

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
		obj.setName(dto.getName());
		obj.setEmail(dto.getEmail());
		obj.setBirthDate(dto.getBirthDate());
		obj.setCredentials(dto.getCredentials());
		
		if (dto.getPosition() != null) {
			Position position = positionRepository.findById(dto.getPosition().getId())
					.orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
			obj.setPosition(position);
			
		}
		if (dto.getDepartment() != null) {
			Set<Department> departments = dto.getDepartment().stream()
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

	        if (dto.getDepartment() != null) {
	            Set<Department> departments = dto.getDepartment().stream()
	                    .map(departmentDto -> departmentRepository.findById(departmentDto.getId())
	                            .orElseThrow(() -> new ResourceNotFoundException("Position not found.")))
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
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
	
			return employeeRepository.findByEmail(username);
		}
		catch(Exception e) {
			throw new UsernameNotFoundException("Email not found.");		}
	}
	
	@Transactional(readOnly = true)
	public EmployeeDTO getMe() {
		Employee emp = authenticated();
		return new EmployeeDTO(emp);
	}
}

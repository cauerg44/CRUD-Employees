package com.dev.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import com.dev.entities.Employee;

public class EmployeeDTO {

	private Long id;
	private String name;
	private String email;
	private LocalDate birthDate;
	private String credentials;

	private PositionDTO position;

	private Set<DepartmentDTO> employeeDepartment;

	public EmployeeDTO() {
	}

	public EmployeeDTO(Long id, String name, String email, LocalDate birthDate, String credentials,
			PositionDTO positionDTO) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
		this.credentials = credentials;
		this.position = positionDTO;
	}

	public EmployeeDTO(Employee obj) {
		id = obj.getId();
		name = obj.getName();
		email = obj.getEmail();
		birthDate = obj.getBirthDate();
		credentials = obj.getCredentials();
		if (obj.getPosition() != null) {
			position = new PositionDTO(obj.getPosition());
		}
		if (obj.getDepartments() != null) {
			employeeDepartment = obj.getDepartments()
					.stream().map(dep -> new DepartmentDTO(dep))
					.collect(Collectors.toSet());
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getCredentials() {
		return credentials;
	}

	public PositionDTO getPosition() {
		return position;
	}

	public Set<DepartmentDTO> getEmployeeDepartment() {
		return employeeDepartment;
	}
}

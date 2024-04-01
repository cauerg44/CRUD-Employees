package com.dev.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import com.dev.entities.Employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class EmployeeDTO {

	private Long id;
	
	@Size(min = 3, max = 50, message = "Name must have 3 up to 50 characters")
    @NotBlank(message = "Employee must have a name.")
	private String name;
	private String email;
	
	@NotNull(message = "Birth date must be provided")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Credentials must not be blank")
    private String credentials;

    @NotNull(message = "Position must be provided")
    private PositionDTO position;

	private Set<DepartmentDTO> department;

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
			department = obj.getDepartments()
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

	public Set<DepartmentDTO> getDepartment() {
		return department;
	}
}

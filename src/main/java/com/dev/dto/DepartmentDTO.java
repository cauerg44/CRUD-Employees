package com.dev.dto;

import com.dev.entities.Department;

public class DepartmentDTO {

	private Long id;
	private String name;
	
	public DepartmentDTO() {
	}

	public DepartmentDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public DepartmentDTO(Department obj) {
		id = obj.getId();
		name = obj.getName();
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

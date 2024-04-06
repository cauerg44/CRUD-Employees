package com.dev.dto;

import com.dev.entities.Position;

import jakarta.validation.constraints.DecimalMin;

public class PositionDTO {

	private Long id;
	private String position;
	private Double salary;
	
	public PositionDTO() {
	}
	
	public PositionDTO(Long id, String position, Double salary) {
		this.id = id;
		this.position = position;
		this.salary = salary;
	}
	
	public PositionDTO(Position obj) {
		id = obj.getId();
		position = obj.getPosition();
		salary = obj.getSalary();
	}

	public Long getId() {
		return id;
	}

	public String getPosition() {
		return position;
	}

	public Double getSalary() {
		return salary;
	}
	
	public Double getAnualIncome () {
		return salary * 12;
	}
}

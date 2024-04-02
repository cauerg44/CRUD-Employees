package com.dev.factories;

import com.dev.entities.Department;

public class DepartmentFactory {

	public static Department createDepartment() {
		return new Department(1L, "Suport");
	}
	
	public static Department createDepartment(Long id, String name) {
		return new Department(id, name);
	}
}

package com.dev.factories;

import java.time.LocalDate;

import com.dev.entities.Department;
import com.dev.entities.Employee;
import com.dev.entities.Role;

public class EmployeeFactory {

	public static Employee createEmployee() {
		Department department = DepartmentFactory.createDepartment();
		Employee employee = new Employee(1L, "Cauê", "caue@gmail.com", LocalDate.parse("2004-11-08"), "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS");
		employee.getDepartments().add(department);
		return employee;
	}
	
	public static Employee createEmployee(String name) {
		Employee employee = createEmployee();
		employee.setName(name);
		return employee;
	}
	
	public static Employee createCustomEmployee(Long id, String email) {
	    Employee employee = new Employee(id, "Cauê Garcia", email, LocalDate.parse("2004-11-08"), "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS");
	    employee.addRole(new Role(2L, "ROLE_EMPLOYEE"));
	    return employee;
	}
	
	public static Employee createCustomCEO(Long id, String email) {
		Employee employee = new Employee(id, "Ricardison Rios", email, LocalDate.parse("1979-12-04"), "$2a$10$9tG6j5avqTb5uvPs.8t84uxS7I3RLa1BGih26RVg9XbfA9J1UQHhS");
		employee.addRole(new Role(1L, "ROLE_CEO"));
		return employee;
	}
}

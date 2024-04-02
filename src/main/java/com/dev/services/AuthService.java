package com.dev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.entities.Employee;
import com.dev.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	@Autowired
	private EmployeeService employeeService;
	
	public void validateSelfOrAdmin(Long employeeId) {
		Employee me = employeeService.authenticated();
		if (me.hasRole("ROLE_CEO")) {
			return;
		}
		if (!me.getId().equals(employeeId)) {
			throw new ForbiddenException("Access denied. Should be self or admin.");
		}
	}
	
}

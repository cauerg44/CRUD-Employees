package com.dev.factories;

import java.util.ArrayList;
import java.util.List;

import com.dev.projections.EmployeeDetailsProjection;

public class EmployeeDetailsFactory {

	public static List<EmployeeDetailsProjection> createCustomEmployee(String username) {

		List<EmployeeDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsImpl(username, "123", 2L, "ROLE_EMPLOYEE"));
		return list;
	}

	public static List<EmployeeDetailsProjection> createCustomCEO(String username) {

		List<EmployeeDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsImpl(username, "123", 1L, "ROLE_CEO"));
		return list;
	}
	
public static List<EmployeeDetailsProjection> createCustomCEOAndEmployee(String username) {
		
		List<EmployeeDetailsProjection> list = new ArrayList<>();
		list.add(new UserDetailsImpl(username, "123", 1L, "ROLE_CEO"));
		list.add(new UserDetailsImpl(username, "123", 2L, "ROLE_EMPLOYEE"));
		return list;
	}
}

class UserDetailsImpl implements EmployeeDetailsProjection {

	private String username;
	private String credentials;
	private Long roleId;
	private String authority;

	public UserDetailsImpl() {
	}

	public UserDetailsImpl(String username, String credentials, Long roleId, String authority) {
		this.username = username;
		this.credentials = credentials;
		this.roleId = roleId;
		this.authority = authority;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getCredentials() {
		return credentials;
	}

	@Override
	public Long getRoleId() {
		return roleId;
	}

	@Override
	public String getAuthority() {
		return authority;
	}
}

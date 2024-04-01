package com.dev.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@Column(unique = true)
	private String email;
	private LocalDate birthDate;
	private String credentials;
	
	@ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;
	
	@ManyToMany
	@JoinTable(name = "employee_department",
				joinColumns = @JoinColumn(name = "employee_id"),
				inverseJoinColumns = @JoinColumn(name = "department_id"))
	private Set<Department> departments = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "employee_role",
	        joinColumns = @JoinColumn(name = "employee_id"),
	        inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	public Employee() {
	}

	public Employee(Long id, String name, String email, LocalDate birthDate, String credentials) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
		this.credentials = credentials;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void addDepartment(Department dep) {
		departments.add(dep);
	}
	
	public void addRole(Role role) {
		roles.add(role);
	}
	
	public boolean hasRole(String roleName) {
		for (Role role : roles) {
			if (role.getAuthority().equals(roleName)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return Objects.equals(id, other.id);
	}
}

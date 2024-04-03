package com.dev.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.entities.Employee;
import com.dev.projections.EmployeeDetailsProjection;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = 
			"SELECT * FROM Employees "
			+ "WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))"
					, nativeQuery = true)
	Page<Employee> searchByName(String name, Pageable page);
	
	@Query(nativeQuery = true, value = """
			SELECT employees.email AS username, employees.credentials, roles.id AS roleId, roles.authority
			FROM employees
			INNER JOIN employee_role ON employees.id = employee_role.employee_id
			INNER JOIN roles ON roles.id = employee_role.role_id
			WHERE employees.email = :email
			""")
	List<EmployeeDetailsProjection> searchEmployeeAndRolesByEmail(String email);
	
	Optional<Employee> findByEmail(String email);
}

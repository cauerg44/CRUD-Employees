package com.dev.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dev.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = 
			"SELECT * FROM Employees "
			+ "WHERE UPPER(name) LIKE UPPER(CONCAT('%', :name, '%'))"
					, nativeQuery = true)
	Page<Employee> searchByName(String name, Pageable page);
}

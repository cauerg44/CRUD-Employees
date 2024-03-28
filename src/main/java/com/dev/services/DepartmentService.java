package com.dev.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.dto.DepartmentDTO;
import com.dev.entities.Department;
import com.dev.repositories.DepartmentRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Transactional(readOnly = true)
	public List<DepartmentDTO> findAll() {
		List<Department> list = departmentRepository.findAll();
		return list.stream().map(x -> new DepartmentDTO(x)).toList();
	}
}

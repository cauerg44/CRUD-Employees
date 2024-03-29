package com.dev.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.dto.PositionDTO;
import com.dev.entities.Position;
import com.dev.repositories.PositionRepository;
import com.dev.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PositionService {

	@Autowired
	private PositionRepository positionRepository;
	
	@Transactional(readOnly = true)
	public List<PositionDTO> findAll() {
		List<Position> list = positionRepository.findAll();
		return list.stream().map(x -> new PositionDTO(x)).toList();
	}
	
	@Transactional
	public PositionDTO insert(PositionDTO dto) {
		Position obj = new Position();
		obj.setPosition(dto.getPosition());
		obj.setSalary(dto.getSalary());
		obj = positionRepository.save(obj);
		return new PositionDTO(obj);
	}
	
	@Transactional
	public PositionDTO update(Long id, PositionDTO dto) {
		try {
			Position obj = positionRepository.getReferenceById(id);
			obj.setPosition(dto.getPosition());
			obj.setSalary(dto.getSalary());
			obj = positionRepository.save(obj);
			return new PositionDTO(obj);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Resource not found.");
		}
	}
}

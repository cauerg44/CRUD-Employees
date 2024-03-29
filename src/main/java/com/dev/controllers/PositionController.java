package com.dev.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dev.dto.PositionDTO;
import com.dev.services.PositionService;

@RestController
@RequestMapping(value = "/positions")
public class PositionController {

	@Autowired
	private PositionService positionService;
	
	@GetMapping
	public ResponseEntity<List<PositionDTO>> findAll() {
		List<PositionDTO> list = positionService.findAll();
		return ResponseEntity.ok(list);
	}
	
	@PostMapping
	public ResponseEntity<PositionDTO> insert(@RequestBody PositionDTO dto) {
		dto = positionService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<PositionDTO> update(@PathVariable Long id, @RequestBody PositionDTO dto) {
		dto = positionService.update(id, dto);
		return ResponseEntity.ok(dto);
	}
}


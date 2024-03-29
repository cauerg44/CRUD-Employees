package com.dev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.entities.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

}

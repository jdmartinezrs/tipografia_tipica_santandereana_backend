package com.miproyecto.backend.ciudad.repository;

import com.miproyecto.backend.ciudad.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
}

package com.miproyecto.backend.pais.repository;

import com.miproyecto.backend.pais.model.Pais;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaisRepository extends JpaRepository<Pais, Long> {
}

package com.miproyecto.backend.peso.repository;

import com.miproyecto.backend.peso.model.Peso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesoRepository extends JpaRepository<Peso, Long> {
}

package com.miproyecto.backend.repository;

import com.miproyecto.backend.model.Peso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesoRepository extends JpaRepository<Peso, Integer> {
}

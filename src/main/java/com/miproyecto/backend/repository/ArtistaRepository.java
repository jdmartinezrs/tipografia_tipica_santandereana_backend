package com.miproyecto.backend.repository;

import com.miproyecto.backend.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Integer> {
}

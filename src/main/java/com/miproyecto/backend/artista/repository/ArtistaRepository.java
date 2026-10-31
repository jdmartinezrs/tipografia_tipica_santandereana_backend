package com.miproyecto.backend.artista.repository;

import com.miproyecto.backend.artista.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
}

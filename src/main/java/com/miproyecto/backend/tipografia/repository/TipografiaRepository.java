package com.miproyecto.backend.tipografia.repository;

import com.miproyecto.backend.tipografia.model.Tipografia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface TipografiaRepository extends JpaRepository<Tipografia, Long> {
    boolean existsByArtista_ArtistaId(Long artistaId);

    @Override
    @EntityGraph(attributePaths = {"categoria", "artista"})
    List<Tipografia> findAll();

    @Override
    @EntityGraph(attributePaths = {"categoria", "artista"})
    Optional<Tipografia> findById(Long id);
}

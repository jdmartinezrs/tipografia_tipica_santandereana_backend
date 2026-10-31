package com.miproyecto.backend.categoria.repository;

import com.miproyecto.backend.categoria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}

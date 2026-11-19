package com.miproyecto.backend.departamento.repository;

import com.miproyecto.backend.departamento.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}

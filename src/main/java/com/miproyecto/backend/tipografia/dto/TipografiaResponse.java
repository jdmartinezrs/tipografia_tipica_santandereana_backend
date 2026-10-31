package com.miproyecto.backend.tipografia.dto;

import com.miproyecto.backend.tipografia.model.Tipografia;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TipografiaResponse(
        Long tipografiaId,
        String nombre,
        String slug,
        String descripcion,
        String imagenPreview,
        BigDecimal precioFamilia,
        RelacionResponse categoria,
        RelacionResponse artista,
        Tipografia.Estado estado,
        Integer version,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion) {

    public record RelacionResponse(Long id, String nombre) {}
}

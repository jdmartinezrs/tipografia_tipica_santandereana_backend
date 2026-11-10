package com.miproyecto.backend.artista.dto;

import com.miproyecto.backend.artista.model.Artista;
import java.time.OffsetDateTime;

public record ArtistaResponse(
        Long artistaId,
        String nombre,
        String pais,
        String biografia,
        String fotoUrl,
        Artista.Estado estado,
        OffsetDateTime fechaRegistro) {
}

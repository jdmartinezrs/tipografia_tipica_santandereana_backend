package com.miproyecto.backend.artista.dto;

import com.miproyecto.backend.artista.model.Artista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArtistaRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String pais,
        String biografia,
        @Size(max = 255) String fotoUrl,
        Artista.Estado estado) {
}

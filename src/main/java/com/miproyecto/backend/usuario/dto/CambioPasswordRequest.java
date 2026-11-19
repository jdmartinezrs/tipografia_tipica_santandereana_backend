package com.miproyecto.backend.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CambioPasswordRequest(
        @NotBlank String passwordActual,
        @NotBlank @Size(min = 8, max = 72) @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d).+", message = "La contraseña debe incluir al menos una letra y un número") String passwordNueva) {
}

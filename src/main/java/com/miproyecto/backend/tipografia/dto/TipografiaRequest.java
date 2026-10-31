package com.miproyecto.backend.tipografia.dto;

import com.miproyecto.backend.tipografia.model.Tipografia;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TipografiaRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 120) String slug,
        String descripcion,
        @Size(max = 255) String imagenPreview,
        @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal precioFamilia,
        @NotNull @Positive Long categoriaId,
        @NotNull @Positive Long artistaId,
        Tipografia.Estado estado) {
}

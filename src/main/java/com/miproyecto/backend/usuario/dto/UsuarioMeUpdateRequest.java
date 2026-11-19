package com.miproyecto.backend.usuario.dto;

import com.miproyecto.backend.usuario.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioMeUpdateRequest(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 100) String apellidos,
        @NotBlank @Email @Size(max = 150) String email,
        Usuario.TipoCliente tipoCliente,
        Long ciudadId,
        @Size(max = 20) String telefono) {
}

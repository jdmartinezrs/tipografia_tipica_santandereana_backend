package com.miproyecto.backend.usuario.dto;

import com.miproyecto.backend.usuario.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 100) String apellidos,
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 150) String email,
        Usuario.Rol rol,
        Usuario.TipoCliente tipoCliente,
        Long ciudadId,
        @Size(max = 20) String telefono,
        Usuario.Estado estado) {
}

package com.miproyecto.backend.usuario.dto;

import com.miproyecto.backend.usuario.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequest(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 100) String apellidos,
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 8, max = 72) @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d).+", message = "La contraseña debe incluir al menos una letra y un número") String password,
        Usuario.Rol rol,
        Usuario.TipoCliente tipoCliente,
        Long ciudadId,
        @Size(max = 20) String telefono) {
}

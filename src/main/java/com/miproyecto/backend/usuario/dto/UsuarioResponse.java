package com.miproyecto.backend.usuario.dto;

import com.miproyecto.backend.usuario.model.Usuario;
import java.time.OffsetDateTime;

public record UsuarioResponse(
        Long usuarioId, String nombre, String apellidos, String username, String email,
        Usuario.Rol rol, Usuario.TipoCliente tipoCliente, CiudadResponse ciudad, String telefono,
        Usuario.Estado estado, short intentosFallidos, OffsetDateTime bloqueadoHasta,
        OffsetDateTime fechaRegistro) {
    public record CiudadResponse(Long ciudadId, String nombre) {}
}

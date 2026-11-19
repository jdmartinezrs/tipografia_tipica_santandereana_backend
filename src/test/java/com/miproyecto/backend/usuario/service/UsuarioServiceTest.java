package com.miproyecto.backend.usuario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.miproyecto.backend.ciudad.repository.CiudadRepository;
import com.miproyecto.backend.usuario.dto.CambioPasswordRequest;
import com.miproyecto.backend.usuario.dto.UsuarioCreateRequest;
import com.miproyecto.backend.usuario.model.Usuario;
import com.miproyecto.backend.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository repository;
    @Mock CiudadRepository ciudadRepository;
    @Mock PasswordEncoder encoder;
    UsuarioService service;
    @BeforeEach void setup() { service = new UsuarioService(repository, ciudadRepository, encoder); }

    @Test void creaUsuarioConPasswordHasheadaYRespuestaSinHash() {
        UsuarioCreateRequest request = new UsuarioCreateRequest("Ana", "Pérez", "ana", "ana@example.com",
                "clave1234", null, Usuario.TipoCliente.PERSONA, null, null);
        when(repository.existsByUsernameIgnoreCase("ana")).thenReturn(false);
        when(repository.existsByEmailIgnoreCase("ana@example.com")).thenReturn(false);
        when(encoder.encode("clave1234")).thenReturn("bcrypt-hash");
        when(repository.save(any())).thenAnswer(invocation -> { Usuario u = invocation.getArgument(0); u.setUsuarioId(9L); return u; });
        var response = service.crear(request);
        assertEquals(Usuario.Rol.CLIENTE, response.rol());
        assertEquals("ana", response.username());
        verify(encoder).encode("clave1234");
        assertFalse(response.toString().contains("bcrypt-hash"));
    }

    @Test void usernameDuplicadoResponde409() {
        when(repository.existsByUsernameIgnoreCase("ana")).thenReturn(true);
        var request = new UsuarioCreateRequest("Ana", null, "ana", "a@example.com", "clave1234", null,
                Usuario.TipoCliente.EMPRESA, null, null);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.crear(request));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test void cambiaPasswordSoloConPasswordActualValida() {
        Usuario user = new Usuario(); user.setPasswordHash("old-hash");
        when(repository.findWithCiudadByUsuarioId(4L)).thenReturn(Optional.of(user));
        when(encoder.matches("actual123", "old-hash")).thenReturn(true);
        when(encoder.encode("nueva1234")).thenReturn("new-hash");
        service.cambiarPassword(4L, new CambioPasswordRequest("actual123", "nueva1234"));
        assertEquals("new-hash", user.getPasswordHash());
    }
}

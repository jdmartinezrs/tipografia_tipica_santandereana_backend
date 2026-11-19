package com.miproyecto.backend.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.miproyecto.backend.auth.model.RefreshToken;
import com.miproyecto.backend.auth.repository.RefreshTokenRepository;
import com.miproyecto.backend.usuario.model.Usuario;
import com.miproyecto.backend.usuario.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock AuthenticationManager authenticationManager;
    @Mock UsuarioRepository usuarioRepository;
    @Mock RefreshTokenRepository refreshRepository;
    @Mock JwtEncoder jwtEncoder;
    AuthService service;
    Usuario usuario;
    @BeforeEach void setup() {
        service = new AuthService(authenticationManager, usuarioRepository, refreshRepository, jwtEncoder);
        ReflectionTestUtils.setField(service, "accessMinutes", 15L);
        ReflectionTestUtils.setField(service, "refreshDays", 7L);
        ReflectionTestUtils.setField(service, "maxAttempts", 3);
        ReflectionTestUtils.setField(service, "lockMinutes", 15L);
        usuario = new Usuario(); usuario.setUsuarioId(1L); usuario.setUsername("ana"); usuario.setRol(Usuario.Rol.CLIENTE);
    }
    private void jwt() { when(jwtEncoder.encode(any())).thenReturn(Jwt.withTokenValue("access-token").header("alg", "HS256").claim("sub", "ana").build()); }

    @Test void loginCorrectoEmiteTokens() {
        when(usuarioRepository.findByUsernameIgnoreCase("ana")).thenReturn(Optional.of(usuario)); jwt();
        var result = service.login("ana", "clave1234");
        assertEquals("Bearer", result.tokenType()); assertEquals(900, result.expiresIn());
        assertNotNull(result.refreshToken()); verify(refreshRepository).save(any(RefreshToken.class));
    }
    @Test void loginInvalidoEsGenericoYCuentaFallo() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));
        when(usuarioRepository.findByUsernameIgnoreCase("ana")).thenReturn(Optional.of(usuario));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.login("ana", "wrong"));
        assertEquals(401, ex.getStatusCode().value()); assertEquals((short) 1, usuario.getIntentosFallidos());
    }
    @Test void cuentaBloqueadaResponde403() {
        when(authenticationManager.authenticate(any())).thenThrow(new LockedException("locked"));
        assertEquals(403, assertThrows(ResponseStatusException.class, () -> service.login("ana", "x")).getStatusCode().value());
    }
    @Test void bloqueaLuegoDelMaximoDeIntentos() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad"));
        when(usuarioRepository.findByUsernameIgnoreCase("ana")).thenReturn(Optional.of(usuario));
        for (int i = 0; i < 3; i++) assertThrows(ResponseStatusException.class, () -> service.login("ana", "wrong"));
        assertEquals((short) 3, usuario.getIntentosFallidos());
        assertTrue(usuario.getBloqueadoHasta().isAfter(OffsetDateTime.now()));
    }
    @Test void refreshRotaTokenYRevocaElAnterior() {
        RefreshToken token = new RefreshToken(); token.setUsuario(usuario); token.setTokenHash("hash");
        token.setFechaExpiracion(OffsetDateTime.now().plusDays(1));
        when(refreshRepository.findLockedByTokenHash(any())).thenReturn(Optional.of(token)); jwt();
        var result = service.refresh("raw-token");
        assertTrue(token.isRevocado()); assertNotNull(result.refreshToken()); verify(refreshRepository, times(2)).save(any());
    }
    @Test void reutilizacionRevocadaRevocaLasSesionesActivas() {
        RefreshToken token = new RefreshToken(); token.setUsuario(usuario); token.setRevocado(true);
        when(refreshRepository.findLockedByTokenHash(any())).thenReturn(Optional.of(token));
        assertEquals(401, assertThrows(ResponseStatusException.class, () -> service.refresh("raw-token")).getStatusCode().value());
        verify(refreshRepository).revokeAllByUsuarioId(1L);
    }
}

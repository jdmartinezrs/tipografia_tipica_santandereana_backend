package com.miproyecto.backend.auth.service;

import com.miproyecto.backend.auth.dto.TokenResponse;
import com.miproyecto.backend.auth.model.RefreshToken;
import com.miproyecto.backend.auth.repository.RefreshTokenRepository;
import com.miproyecto.backend.usuario.model.Usuario;
import com.miproyecto.backend.usuario.repository.UsuarioRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshRepository;
    private final JwtEncoder jwtEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value("${app.jwt.access-token-minutes:15}") private long accessMinutes;
    @Value("${app.jwt.refresh-token-days:7}") private long refreshDays;
    @Value("${app.auth.max-failed-attempts:5}") private int maxAttempts;
    @Value("${app.auth.lock-minutes:15}") private long lockMinutes;
    public AuthService(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository,
            RefreshTokenRepository refreshRepository, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager; this.usuarioRepository = usuarioRepository;
        this.refreshRepository = refreshRepository; this.jwtEncoder = jwtEncoder;
    }
    @Transactional(noRollbackFor = ResponseStatusException.class) public TokenResponse login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (LockedException | DisabledException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta está bloqueada o inactiva");
        } catch (AuthenticationException ex) {
            usuarioRepository.findByUsernameIgnoreCase(username).ifPresent(this::registrarFallo);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));
        usuario.setIntentosFallidos((short) 0); usuario.setBloqueadoHasta(null);
        usuarioRepository.save(usuario);
        return emitir(usuario);
    }
    @Transactional(noRollbackFor = ResponseStatusException.class) public TokenResponse refresh(String rawToken) {
        String hash = sha256(rawToken);
        RefreshToken token = refreshRepository.findLockedByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token inválido"));
        Usuario usuario = token.getUsuario();
        if (token.isRevocado()) {
            refreshRepository.revokeAllByUsuarioId(usuario.getUsuarioId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token reutilizado; sesiones revocadas");
        }
        if (!token.getFechaExpiracion().isAfter(OffsetDateTime.now())) {
            token.setRevocado(true); refreshRepository.save(token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expirado");
        }
        token.setRevocado(true); refreshRepository.save(token);
        return emitir(usuario);
    }
    @Transactional public void logout(String rawToken) {
        refreshRepository.findByTokenHash(sha256(rawToken)).ifPresent(token -> {
            token.setRevocado(true); refreshRepository.save(token);
        });
    }
    @Transactional public void logoutAll(Long usuarioId) { refreshRepository.revokeAllByUsuarioId(usuarioId); }
    private void registrarFallo(Usuario usuario) {
        int count = Math.min(Short.MAX_VALUE, usuario.getIntentosFallidos() + 1);
        usuario.setIntentosFallidos((short) count);
        if (count >= maxAttempts) usuario.setBloqueadoHasta(OffsetDateTime.now().plusMinutes(lockMinutes));
        usuarioRepository.save(usuario);
    }
    private TokenResponse emitir(Usuario usuario) {
        OffsetDateTime now = OffsetDateTime.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("tipografia-api").issuedAt(now.toInstant())
                .expiresAt(now.plusMinutes(accessMinutes).toInstant()).subject(usuario.getUsername())
                .claim("id", usuario.getUsuarioId()).claim("roles", java.util.List.of(usuario.getRol().name())).build();
        String access = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        byte[] random = new byte[64]; secureRandom.nextBytes(random);
        String refresh = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        RefreshToken entity = new RefreshToken(); entity.setUsuario(usuario); entity.setTokenHash(sha256(refresh));
        entity.setFechaCreacion(now); entity.setFechaExpiracion(now.plusDays(refreshDays)); entity.setRevocado(false);
        refreshRepository.save(entity);
        return new TokenResponse(access, refresh, "Bearer", accessMinutes * 60);
    }
    private String sha256(String value) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new IllegalStateException("SHA-256 no disponible", e); }
    }
}

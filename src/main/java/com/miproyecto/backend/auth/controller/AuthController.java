package com.miproyecto.backend.auth.controller;

import com.miproyecto.backend.auth.dto.*;
import com.miproyecto.backend.auth.service.AuthService;
import com.miproyecto.backend.usuario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Authorization", "Content-Type"})
public class AuthController {
    private final AuthService service;
    private final UsuarioRepository usuarioRepository;
    public AuthController(AuthService service, UsuarioRepository usuarioRepository) { this.service = service; this.usuarioRepository = usuarioRepository; }
    @PostMapping("/login") public TokenResponse login(@Valid @RequestBody LoginRequest r) { return service.login(r.username(), r.password()); }
    @PostMapping("/refresh") public TokenResponse refresh(@Valid @RequestBody RefreshRequest r) { return service.refresh(r.refreshToken()); }
    @PostMapping("/logout") public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest r) { service.logout(r.refreshToken()); return ResponseEntity.noContent().build(); }
    @PostMapping("/logout-all") public ResponseEntity<Void> logoutAll(Authentication auth) {
        Long id = usuarioRepository.findByUsernameIgnoreCase(auth.getName()).orElseThrow().getUsuarioId();
        service.logoutAll(id); return ResponseEntity.noContent().build();
    }
}

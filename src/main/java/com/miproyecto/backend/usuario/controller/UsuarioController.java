package com.miproyecto.backend.usuario.controller;

import com.miproyecto.backend.usuario.dto.*;
import com.miproyecto.backend.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = {"Authorization", "Content-Type"})
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @GetMapping public Page<UsuarioResponse> listar(Pageable pageable) { return service.listar(pageable); }
    @GetMapping("/me") public UsuarioResponse me(Authentication a) { return service.obtenerPorUsername(a.getName()); }
    @PutMapping("/me") public UsuarioResponse actualizarMe(Authentication a, @Valid @RequestBody UsuarioMeUpdateRequest r) { return service.actualizarMe(a.getName(), r); }
    @PutMapping("/me/password") public ResponseEntity<Void> cambiarMiPassword(Authentication a, @Valid @RequestBody CambioPasswordRequest r) {
        service.cambiarMiPassword(a.getName(), r); return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}") public UsuarioResponse obtener(@PathVariable Long id) { return service.obtener(id); }
    @PostMapping public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateRequest r, Authentication authentication) {
        boolean admin = authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(r, admin));
    }
    @PutMapping("/{id}") public UsuarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest r) { return service.actualizar(id, r); }
    @PutMapping("/{id}/password") public ResponseEntity<Void> cambiarPassword(@PathVariable Long id, @Valid @RequestBody CambioPasswordRequest r) {
        service.cambiarPassword(id, r); return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/bloqueo") public ResponseEntity<Void> bloquear(@PathVariable Long id, @RequestParam boolean bloqueado) {
        service.cambiarBloqueo(id, bloqueado); return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id) { service.eliminar(id); return ResponseEntity.noContent().build(); }
}

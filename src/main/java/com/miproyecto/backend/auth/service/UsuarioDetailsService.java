package com.miproyecto.backend.auth.service;

import com.miproyecto.backend.usuario.model.Usuario;
import com.miproyecto.backend.usuario.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository repository;
    public UsuarioDetailsService(UsuarioRepository repository) { this.repository = repository; }
    @Override public UserDetails loadUserByUsername(String username) {
        Usuario usuario = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas"));
        boolean bloqueado = usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(OffsetDateTime.now());
        return User.withUsername(usuario.getUsername()).password(usuario.getPasswordHash())
                .authorities("ROLE_" + usuario.getRol().name()).disabled(usuario.getEstado() != Usuario.Estado.ACTIVO)
                .accountLocked(bloqueado).build();
    }
}

package com.miproyecto.backend.usuario.repository;

import com.miproyecto.backend.usuario.model.Usuario;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCaseAndUsuarioIdNot(String username, Long usuarioId);
    boolean existsByEmailIgnoreCaseAndUsuarioIdNot(String email, Long usuarioId);
    Optional<Usuario> findByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = "ciudad")
    Optional<Usuario> findWithCiudadByUsuarioId(Long usuarioId);

    @EntityGraph(attributePaths = "ciudad")
    Optional<Usuario> findWithCiudadByUsernameIgnoreCase(String username);

    @EntityGraph(attributePaths = "ciudad")
    Page<Usuario> findAllBy(Pageable pageable);
}

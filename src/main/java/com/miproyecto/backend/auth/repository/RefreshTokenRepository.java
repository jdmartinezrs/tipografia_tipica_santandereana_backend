package com.miproyecto.backend.auth.repository;

import com.miproyecto.backend.auth.model.RefreshToken;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from RefreshToken t join fetch t.usuario where t.tokenHash = :tokenHash")
    Optional<RefreshToken> findLockedByTokenHash(String tokenHash);
    @Modifying @Query("update RefreshToken t set t.revocado = true where t.usuario.usuarioId = :usuarioId and t.revocado = false")
    void revokeAllByUsuarioId(Long usuarioId);
    void deleteByFechaExpiracionBefore(OffsetDateTime fecha);
}

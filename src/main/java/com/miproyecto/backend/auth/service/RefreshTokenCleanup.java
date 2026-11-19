package com.miproyecto.backend.auth.service;

import com.miproyecto.backend.auth.repository.RefreshTokenRepository;
import java.time.OffsetDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RefreshTokenCleanup {
    private final RefreshTokenRepository repository;
    public RefreshTokenCleanup(RefreshTokenRepository repository) { this.repository = repository; }
    @Scheduled(fixedDelayString = "${app.auth.cleanup-delay-ms:3600000}")
    @Transactional public void eliminarExpirados() { repository.deleteByFechaExpiracionBefore(OffsetDateTime.now()); }
}

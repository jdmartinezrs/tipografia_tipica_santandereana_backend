package com.miproyecto.backend.auth.model;

import com.miproyecto.backend.usuario.model.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "refresh_token_id") private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id", nullable = false) private Usuario usuario;
    @Column(name = "token_hash", nullable = false, unique = true, length = 64) private String tokenHash;
    @Column(name = "fecha_expiracion", nullable = false) private OffsetDateTime fechaExpiracion;
    @Column(name = "revocado", nullable = false) private boolean revocado;
    @Column(name = "fecha_creacion", nullable = false) private OffsetDateTime fechaCreacion;
    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public OffsetDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(OffsetDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public boolean isRevocado() { return revocado; }
    public void setRevocado(boolean revocado) { this.revocado = revocado; }
    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

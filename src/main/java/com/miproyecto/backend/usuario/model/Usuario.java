package com.miproyecto.backend.usuario.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id") private Long usuarioId;
    @Column(name = "nombre", nullable = false, length = 100) private String nombre;
    @Column(name = "email", nullable = false, length = 150) private String email;
    @Column(name = "password_hash", nullable = false, length = 255) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(name = "rol", nullable = false, length = 10) private Rol rol = Rol.CLIENTE;
    @Enumerated(EnumType.STRING) @Column(name = "tipo_cliente", length = 10) private TipoCliente tipoCliente;
    @Column(name = "ciudad", length = 80) private String ciudad;
    @Column(name = "telefono", length = 20) private String telefono;
    @Enumerated(EnumType.STRING) @Column(name = "estado", nullable = false, length = 10) private Estado estado = Estado.ACTIVO;
    @Column(name = "intentos_fallidos", nullable = false) private Short intentosFallidos = 0;
    @Column(name = "bloqueado_hasta") private OffsetDateTime bloqueadoHasta;
    @Column(name = "fecha_registro", nullable = false) private OffsetDateTime fechaRegistro = OffsetDateTime.now();
    public enum Rol { ADMIN, CLIENTE }
    public enum TipoCliente { PERSONA, EMPRESA, AGENCIA }
    public enum Estado { ACTIVO, INACTIVO }
    public Usuario() {}
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long id) { usuarioId = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String v) { nombre = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String v) { passwordHash = v; }
    public Rol getRol() { return rol; }
    public void setRol(Rol v) { rol = v; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente v) { tipoCliente = v; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String v) { ciudad = v; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String v) { telefono = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { estado = v; }
    public Short getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Short v) { intentosFallidos = v; }
    public OffsetDateTime getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(OffsetDateTime v) { bloqueadoHasta = v; }
    public OffsetDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(OffsetDateTime v) { fechaRegistro = v; }
}

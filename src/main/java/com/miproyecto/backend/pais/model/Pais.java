package com.miproyecto.backend.pais.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "pais")
public class Pais {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pais_id")
    private Long paisId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "codigo_iso", nullable = false, length = 2, unique = true)
    private String codigoIso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    public enum Estado { ACTIVO, INACTIVO }

    public Pais() {}

    public Long getPaisId() { return paisId; }
    public void setPaisId(Long paisId) { this.paisId = paisId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigoIso() { return codigoIso; }
    public void setCodigoIso(String codigoIso) { this.codigoIso = codigoIso; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}

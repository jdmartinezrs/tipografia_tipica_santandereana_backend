package com.miproyecto.backend.licencia.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "licencia")
public class Licencia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "licencia_id") private Long licenciaId;
    @Enumerated(EnumType.STRING) @Column(name = "tipo", nullable = false, length = 20, unique = true) private Tipo tipo;
    @Column(name = "descripcion", nullable = false, length = 150) private String descripcion;
    @Column(name = "precio_extra", nullable = false, precision = 12, scale = 2) private BigDecimal precioExtra = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(name = "estado", nullable = false, length = 10) private Estado estado = Estado.ACTIVA;
    public enum Tipo { PERSONAL, COMERCIAL, EMPRESARIAL, WEB, EDITORIAL }
    public enum Estado { ACTIVA, INACTIVA }
    public Licencia() {}
    public Long getLicenciaId() { return licenciaId; }
    public void setLicenciaId(Long v) { licenciaId = v; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo v) { tipo = v; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { descripcion = v; }
    public BigDecimal getPrecioExtra() { return precioExtra; }
    public void setPrecioExtra(BigDecimal v) { precioExtra = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { estado = v; }
}

package com.miproyecto.backend.peso.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "peso")
public class Peso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peso_id")
    private Long pesoId;

    @Column(name = "nombre", nullable = false, length = 30, unique = true)
    private String nombre;

    @Column(name = "grosor", nullable = false)
    private Short grosor;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "orden", nullable = false)
    private Short orden;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    public enum Estado { ACTIVO, INACTIVO }

    public Peso() {}

    public Peso(String nombre) {
        this.nombre = nombre;
    }

    public Long getPesoId() { return pesoId; }
    public void setPesoId(Long pesoId) { this.pesoId = pesoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Short getGrosor() { return grosor; }
    public void setGrosor(Short grosor) { this.grosor = grosor; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Short getOrden() { return orden; }
    public void setOrden(Short orden) { this.orden = orden; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}

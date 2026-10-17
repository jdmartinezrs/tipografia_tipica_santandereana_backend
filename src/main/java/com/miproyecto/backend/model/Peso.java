package com.miproyecto.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "peso")
public class Peso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pesoId;

    @Column(nullable = false, length = 50)
    private String nombre;

    public Peso() {}

    public Peso(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPesoId() { return pesoId; }
    public void setPesoId(Integer pesoId) { this.pesoId = pesoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

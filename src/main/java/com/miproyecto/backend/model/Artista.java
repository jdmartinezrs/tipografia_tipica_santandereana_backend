package com.miproyecto.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistaId;

    @Column(nullable = false, length = 100)
    private String nombre;

    public Artista() {}

    public Artista(String nombre) {
        this.nombre = nombre;
    }

    public Integer getArtistaId() { return artistaId; }
    public void setArtistaId(Integer artistaId) { this.artistaId = artistaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

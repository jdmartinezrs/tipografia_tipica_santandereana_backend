package com.miproyecto.backend.artista.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.OffsetDateTime;

@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artista_id")
    private Long artistaId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "pais", nullable = false, length = 100)
    private String pais;

    @Column(name = "biografia")
    private String biografia;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "fecha_registro", nullable = false)
    private OffsetDateTime fechaRegistro = OffsetDateTime.now();

    public enum Estado { ACTIVO, INACTIVO }

    public Artista() {}

    public Artista(String nombre) {
        this.nombre = nombre;
    }

    public Long getArtistaId() { return artistaId; }
    public void setArtistaId(Long artistaId) { this.artistaId = artistaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public OffsetDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(OffsetDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}

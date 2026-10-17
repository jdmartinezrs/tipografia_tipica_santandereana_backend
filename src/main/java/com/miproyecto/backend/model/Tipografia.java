package com.miproyecto.backend.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipografia")
public class Tipografia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tipografiaId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 20)
    private CategoriaTipografia categoria;

    @ManyToOne
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

    @ManyToOne
    @JoinColumn(name = "peso_id", nullable = false)
    private Peso peso;

    public Tipografia() {}

    public Tipografia(String nombre, BigDecimal precio, CategoriaTipografia categoria, Artista artista, Peso peso) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.artista = artista;
        this.peso = peso;
    }

    public Integer getTipografiaId() { return tipografiaId; }
    public void setTipografiaId(Integer tipografiaId) { this.tipografiaId = tipografiaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public CategoriaTipografia getCategoria() { return categoria; }
    public void setCategoria(CategoriaTipografia categoria) { this.categoria = categoria; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public Peso getPeso() { return peso; }
    public void setPeso(Peso peso) { this.peso = peso; }
}

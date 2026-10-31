package com.miproyecto.backend.tipografia.model;

import com.miproyecto.backend.artista.model.Artista;
import com.miproyecto.backend.categoria.model.Categoria;
import com.miproyecto.backend.peso.model.Peso;
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
import jakarta.persistence.FetchType;
import jakarta.persistence.Version;
import jakarta.persistence.Transient;
import jakarta.persistence.PreUpdate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tipografia")
public class Tipografia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipografia_id")
    private Long tipografiaId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "slug", nullable = false, length = 120, unique = true)
    private String slug;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "imagen_preview", length = 255)
    private String imagenPreview;

    @Column(name = "precio_familia", precision = 12, scale = 2)
    private BigDecimal precioFamilia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artista_id", nullable = false)
    private Artista artista;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private Estado estado = Estado.OCULTA;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 0;

    @Column(name = "fecha_creacion", nullable = false)
    private OffsetDateTime fechaCreacion = OffsetDateTime.now();

    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion = OffsetDateTime.now();

    @Transient
    private Peso peso;

    public enum Estado { PUBLICADA, OCULTA }

    @PreUpdate
    void actualizarFecha() { fechaActualizacion = OffsetDateTime.now(); }

    public Tipografia() {}

    public Tipografia(String nombre, BigDecimal precio, CategoriaTipografia categoria, Artista artista, Peso peso) {
        this.nombre = nombre;
        this.artista = artista;
        if (categoria != null) this.categoria = new Categoria(categoria.getDisplayName());
        this.precioFamilia = precio;
        this.peso = peso;
    }

    public Long getTipografiaId() { return tipografiaId; }
    public void setTipografiaId(Long tipografiaId) { this.tipografiaId = tipografiaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getPrecio() { return precioFamilia; }
    public void setPrecio(BigDecimal precio) { this.precioFamilia = precio; }
    public BigDecimal getPrecioFamilia() { return precioFamilia; }
    public void setPrecioFamilia(BigDecimal precioFamilia) { this.precioFamilia = precioFamilia; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenPreview() { return imagenPreview; }
    public void setImagenPreview(String imagenPreview) { this.imagenPreview = imagenPreview; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Integer getVersion() { return version; }
    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public OffsetDateTime getFechaActualizacion() { return fechaActualizacion; }
    public Peso getPeso() { return peso; }
    public void setPeso(Peso peso) { this.peso = peso; }
}

package com.miproyecto.backend.tipografia.model;

import com.miproyecto.backend.peso.model.Peso;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "variante", uniqueConstraints = @UniqueConstraint(name = "ux_variante", columnNames = {"tipografia_id", "peso_id"}))
public class Variante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variante_id") private Long varianteId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "tipografia_id", nullable = false) private Tipografia tipografia;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "peso_id", nullable = false) private Peso peso;
    @Column(name = "precio", nullable = false, precision = 12, scale = 2) private BigDecimal precio;
    @Column(name = "preview_url", length = 255) private String previewUrl;
    public Variante() {}
    public Long getVarianteId() { return varianteId; }
    public void setVarianteId(Long v) { varianteId = v; }
    public Tipografia getTipografia() { return tipografia; }
    public void setTipografia(Tipografia v) { tipografia = v; }
    public Peso getPeso() { return peso; }
    public void setPeso(Peso v) { peso = v; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal v) { precio = v; }
    public String getPreviewUrl() { return previewUrl; }
    public void setPreviewUrl(String v) { previewUrl = v; }
}

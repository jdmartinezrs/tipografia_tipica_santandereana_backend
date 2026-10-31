package com.miproyecto.backend.compra.model;

import com.miproyecto.backend.licencia.model.Licencia;
import com.miproyecto.backend.tipografia.model.Tipografia;
import com.miproyecto.backend.tipografia.model.Variante;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_compra")
public class DetalleCompra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_id") private Long detalleId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "compra_id", nullable = false) private Compra compra;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "tipografia_id", nullable = false) private Tipografia tipografia;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "variante_id") private Variante variante;
    @Column(name = "es_familia", nullable = false) private boolean esFamilia;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "licencia_id", nullable = false) private Licencia licencia;
    @Column(name = "precio_base", nullable = false, precision = 12, scale = 2) private BigDecimal precioBase;
    @Column(name = "precio_licencia", nullable = false, precision = 12, scale = 2) private BigDecimal precioLicencia = BigDecimal.ZERO;
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2) private BigDecimal subtotal;
    @Column(name = "porcentaje_artista", nullable = false, precision = 5, scale = 2) private BigDecimal porcentajeArtista = new BigDecimal("70.00");
    @Column(name = "regalia_artista", nullable = false, precision = 12, scale = 2) private BigDecimal regaliaArtista;
    @Column(name = "regalia_casa", nullable = false, precision = 12, scale = 2) private BigDecimal regaliaCasa;
    public DetalleCompra() {}
    public Long getDetalleId() { return detalleId; }
    public void setDetalleId(Long v) { detalleId = v; }
    public Compra getCompra() { return compra; }
    public void setCompra(Compra v) { compra = v; }
    public Tipografia getTipografia() { return tipografia; }
    public void setTipografia(Tipografia v) { tipografia = v; }
    public Variante getVariante() { return variante; }
    public void setVariante(Variante v) { variante = v; }
    public boolean isEsFamilia() { return esFamilia; }
    public void setEsFamilia(boolean v) { esFamilia = v; }
    public Licencia getLicencia() { return licencia; }
    public void setLicencia(Licencia v) { licencia = v; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public void setPrecioBase(BigDecimal v) { precioBase = v; }
    public BigDecimal getPrecioLicencia() { return precioLicencia; }
    public void setPrecioLicencia(BigDecimal v) { precioLicencia = v; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal v) { subtotal = v; }
    public BigDecimal getPorcentajeArtista() { return porcentajeArtista; }
    public void setPorcentajeArtista(BigDecimal v) { porcentajeArtista = v; }
    public BigDecimal getRegaliaArtista() { return regaliaArtista; }
    public void setRegaliaArtista(BigDecimal v) { regaliaArtista = v; }
    public BigDecimal getRegaliaCasa() { return regaliaCasa; }
    public void setRegaliaCasa(BigDecimal v) { regaliaCasa = v; }
}

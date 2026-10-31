package com.miproyecto.backend.compra.model;

import com.miproyecto.backend.usuario.model.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "compra")
public class Compra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_id") private Long compraId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id", nullable = false) private Usuario usuario;
    @Column(name = "fecha", nullable = false) private OffsetDateTime fecha = OffsetDateTime.now();
    @Column(name = "total", nullable = false, precision = 12, scale = 2) private BigDecimal total;
    @Column(name = "moneda", nullable = false, length = 3) private String moneda = "COP";
    @Enumerated(EnumType.STRING) @Column(name = "estado", nullable = false, length = 10) private Estado estado = Estado.PENDIENTE;
    @Enumerated(EnumType.STRING) @Column(name = "metodo_pago", length = 20) private MetodoPago metodoPago;
    @Column(name = "referencia_pago", length = 100) private String referenciaPago;
    @Column(name = "fecha_pago") private OffsetDateTime fechaPago;
    public enum Estado { PENDIENTE, PAGADA, FALLIDA, CANCELADA }
    public enum MetodoPago { TARJETA, PSE, NEQUI }
    public Compra() {}
    public Long getCompraId() { return compraId; }
    public void setCompraId(Long v) { compraId = v; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario v) { usuario = v; }
    public OffsetDateTime getFecha() { return fecha; }
    public void setFecha(OffsetDateTime v) { fecha = v; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal v) { total = v; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String v) { moneda = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { estado = v; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago v) { metodoPago = v; }
    public String getReferenciaPago() { return referenciaPago; }
    public void setReferenciaPago(String v) { referenciaPago = v; }
    public OffsetDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(OffsetDateTime v) { fechaPago = v; }
}

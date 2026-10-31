package com.miproyecto.backend.descarga.model;

import com.miproyecto.backend.compra.model.DetalleCompra;
import com.miproyecto.backend.tipografia.model.FontFile;
import com.miproyecto.backend.usuario.model.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "descarga")
public class Descarga {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "descarga_id") private Long descargaId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "usuario_id", nullable = false) private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "file_id", nullable = false) private FontFile fontFile;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "detalle_id", nullable = false) private DetalleCompra detalleCompra;
    @Column(name = "fecha", nullable = false) private OffsetDateTime fecha = OffsetDateTime.now();
    @Column(name = "ip", length = 45) private String ip;
    public Descarga() {}
    public Long getDescargaId() { return descargaId; }
    public void setDescargaId(Long v) { descargaId = v; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario v) { usuario = v; }
    public FontFile getFontFile() { return fontFile; }
    public void setFontFile(FontFile v) { fontFile = v; }
    public DetalleCompra getDetalleCompra() { return detalleCompra; }
    public void setDetalleCompra(DetalleCompra v) { detalleCompra = v; }
    public OffsetDateTime getFecha() { return fecha; }
    public void setFecha(OffsetDateTime v) { fecha = v; }
    public String getIp() { return ip; }
    public void setIp(String v) { ip = v; }
}

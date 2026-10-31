package com.miproyecto.backend.tipografia.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "font_file", uniqueConstraints = @UniqueConstraint(name = "ux_ff", columnNames = {"variante_id", "formato"}))
public class FontFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id") private Long fileId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "variante_id", nullable = false) private Variante variante;
    @Enumerated(EnumType.STRING) @Column(name = "formato", nullable = false, length = 5) private Formato formato;
    @Column(name = "tamano_kb", nullable = false, precision = 10, scale = 2) private BigDecimal tamanoKb;
    @Column(name = "storage_key", nullable = false, length = 255) private String storageKey;
    @Column(name = "checksum_sha256", length = 64) private String checksumSha256;
    @Enumerated(EnumType.STRING) @Column(name = "estado", nullable = false, length = 12) private Estado estado = Estado.DISPONIBLE;
    @Column(name = "fecha_subida", nullable = false) private OffsetDateTime fechaSubida = OffsetDateTime.now();
    public enum Formato { WOFF2, WOFF, TTF, OTF }
    public enum Estado { DISPONIBLE, ARCHIVADO }
    public FontFile() {}
    public Long getFileId() { return fileId; }
    public void setFileId(Long v) { fileId = v; }
    public Variante getVariante() { return variante; }
    public void setVariante(Variante v) { variante = v; }
    public Formato getFormato() { return formato; }
    public void setFormato(Formato v) { formato = v; }
    public BigDecimal getTamanoKb() { return tamanoKb; }
    public void setTamanoKb(BigDecimal v) { tamanoKb = v; }
    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String v) { storageKey = v; }
    public String getChecksumSha256() { return checksumSha256; }
    public void setChecksumSha256(String v) { checksumSha256 = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { estado = v; }
    public OffsetDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(OffsetDateTime v) { fechaSubida = v; }
}

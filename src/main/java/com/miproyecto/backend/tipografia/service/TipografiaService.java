package com.miproyecto.backend.tipografia.service;

import com.miproyecto.backend.tipografia.dto.TipografiaRequest;
import com.miproyecto.backend.tipografia.dto.TipografiaResponse;
import com.miproyecto.backend.artista.model.Artista;
import com.miproyecto.backend.categoria.model.Categoria;
import com.miproyecto.backend.tipografia.model.Tipografia;
import com.miproyecto.backend.artista.repository.ArtistaRepository;
import com.miproyecto.backend.categoria.repository.CategoriaRepository;
import com.miproyecto.backend.tipografia.repository.TipografiaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class TipografiaService {
    private final TipografiaRepository tipografiaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ArtistaRepository artistaRepository;

    public TipografiaService(TipografiaRepository tipografiaRepository,
            CategoriaRepository categoriaRepository, ArtistaRepository artistaRepository) {
        this.tipografiaRepository = tipografiaRepository;
        this.categoriaRepository = categoriaRepository;
        this.artistaRepository = artistaRepository;
    }

    public List<TipografiaResponse> listarTodas() {
        return tipografiaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public TipografiaResponse obtenerPorId(Long id) {
        return tipografiaRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public TipografiaResponse crear(TipografiaRequest request) {
        Tipografia tipografia = new Tipografia();
        aplicar(request, tipografia);
        return toResponse(tipografiaRepository.save(tipografia));
    }

    @Transactional
    public TipografiaResponse actualizar(Long id, TipografiaRequest request) {
        Tipografia tipografia = tipografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        aplicar(request, tipografia);
        return toResponse(tipografiaRepository.save(tipografia));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!tipografiaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        tipografiaRepository.deleteById(id);
    }

    private void aplicar(TipografiaRequest request, Tipografia tipografia) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        Artista artista = artistaRepository.findById(request.artistaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado"));

        tipografia.setNombre(request.nombre());
        tipografia.setSlug(request.slug());
        tipografia.setDescripcion(request.descripcion());
        tipografia.setImagenPreview(request.imagenPreview());
        tipografia.setPrecioFamilia(request.precioFamilia());
        tipografia.setCategoria(categoria);
        tipografia.setArtista(artista);
        tipografia.setEstado(request.estado() == null ? Tipografia.Estado.OCULTA : request.estado());
    }

    private TipografiaResponse toResponse(Tipografia tipografia) {
        return new TipografiaResponse(
                tipografia.getTipografiaId(), tipografia.getNombre(), tipografia.getSlug(),
                tipografia.getDescripcion(), tipografia.getImagenPreview(), tipografia.getPrecioFamilia(),
                new TipografiaResponse.RelacionResponse(tipografia.getCategoria().getCategoriaId(),
                        tipografia.getCategoria().getNombre()),
                new TipografiaResponse.RelacionResponse(tipografia.getArtista().getArtistaId(),
                        tipografia.getArtista().getNombre()),
                tipografia.getEstado(), tipografia.getVersion(), tipografia.getFechaCreacion(),
                tipografia.getFechaActualizacion());
    }
}

package com.miproyecto.backend.artista.service;

import com.miproyecto.backend.artista.dto.ArtistaRequest;
import com.miproyecto.backend.artista.dto.ArtistaResponse;
import com.miproyecto.backend.artista.model.Artista;
import com.miproyecto.backend.artista.repository.ArtistaRepository;
import com.miproyecto.backend.tipografia.repository.TipografiaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class ArtistaService {
    private final ArtistaRepository repository;
    private final TipografiaRepository tipografiaRepository;

    public ArtistaService(ArtistaRepository repository, TipografiaRepository tipografiaRepository) {
        this.repository = repository;
        this.tipografiaRepository = tipografiaRepository;
    }

    public List<ArtistaResponse> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public ArtistaResponse obtenerPorId(Long id) {
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado"));
    }

    @Transactional
    public ArtistaResponse crear(ArtistaRequest request) {
        Artista artista = new Artista();
        aplicar(request, artista);
        return toResponse(repository.save(artista));
    }

    @Transactional
    public ArtistaResponse actualizar(Long id, ArtistaRequest request) {
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado"));
        aplicar(request, artista);
        return toResponse(repository.save(artista));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado");
        }
        if (tipografiaRepository.existsByArtista_ArtistaId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar el artista porque tiene tipografías asociadas");
        }
        repository.deleteById(id);
    }

    private void aplicar(ArtistaRequest request, Artista artista) {
        artista.setNombre(request.nombre());
        artista.setPais(request.pais());
        artista.setBiografia(request.biografia());
        artista.setFotoUrl(request.fotoUrl());
        artista.setEstado(request.estado() == null ? Artista.Estado.ACTIVO : request.estado());
    }

    private ArtistaResponse toResponse(Artista artista) {
        return new ArtistaResponse(artista.getArtistaId(), artista.getNombre(), artista.getPais(),
                artista.getBiografia(), artista.getFotoUrl(), artista.getEstado(), artista.getFechaRegistro());
    }
}

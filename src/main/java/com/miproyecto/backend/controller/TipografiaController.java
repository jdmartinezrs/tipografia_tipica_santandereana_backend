package com.miproyecto.backend.controller;

import com.miproyecto.backend.model.Tipografia;
import com.miproyecto.backend.repository.TipografiaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tipografias")
@CrossOrigin(origins = "http://localhost:4200")
public class TipografiaController {
    private final TipografiaRepository repository;

    public TipografiaController(TipografiaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tipografia> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tipografia> obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tipografia> crear(@RequestBody Tipografia tipografia) {
        tipografia.setTipografiaId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tipografia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tipografia> actualizar(@PathVariable Integer id, @RequestBody Tipografia datos) {
        return repository.findById(id).map(actual -> {
            actual.setNombre(datos.getNombre());
            actual.setPrecio(datos.getPrecio());
            actual.setCategoria(datos.getCategoria());
            actual.setArtista(datos.getArtista());
            actual.setPeso(datos.getPeso());
            return ResponseEntity.ok(repository.save(actual));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

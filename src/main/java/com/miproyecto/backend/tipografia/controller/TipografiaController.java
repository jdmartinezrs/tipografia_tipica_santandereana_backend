package com.miproyecto.backend.tipografia.controller;

import com.miproyecto.backend.tipografia.dto.TipografiaRequest;
import com.miproyecto.backend.tipografia.dto.TipografiaResponse;
import com.miproyecto.backend.tipografia.service.TipografiaService;
import jakarta.validation.Valid;
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
    private final TipografiaService service;

    public TipografiaController(TipografiaService service) {
        this.service = service;
    }

    @GetMapping
    public List<TipografiaResponse> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipografiaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipografiaResponse> crear(@Valid @RequestBody TipografiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipografiaResponse> actualizar(@PathVariable Long id,
            @Valid @RequestBody TipografiaRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

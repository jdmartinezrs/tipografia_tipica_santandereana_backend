package com.miproyecto.backend.config;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> responseStatus(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return body(status, ex.getReason() == null ? status.getReasonPhrase() : ex.getReason(), request);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
                .distinct().collect(Collectors.joining("; "));
        return body(HttpStatus.BAD_REQUEST, message.isBlank() ? "Solicitud inválida" : message, request);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return body(HttpStatus.CONFLICT, "El registro entra en conflicto con datos existentes", request);
    }
    private ResponseEntity<Map<String, Object>> body(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now().toString(), "status", status.value(),
                "error", status.getReasonPhrase(), "message", message, "path", request.getRequestURI()));
    }
}

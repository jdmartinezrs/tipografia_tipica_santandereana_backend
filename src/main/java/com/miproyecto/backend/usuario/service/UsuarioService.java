package com.miproyecto.backend.usuario.service;

import com.miproyecto.backend.ciudad.model.Ciudad;
import com.miproyecto.backend.ciudad.repository.CiudadRepository;
import com.miproyecto.backend.usuario.dto.*;
import com.miproyecto.backend.usuario.model.Usuario;
import com.miproyecto.backend.usuario.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class UsuarioService {
    private final UsuarioRepository repository;
    private final CiudadRepository ciudadRepository;
    private final PasswordEncoder passwordEncoder;
    public UsuarioService(UsuarioRepository repository, CiudadRepository ciudadRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository; this.ciudadRepository = ciudadRepository; this.passwordEncoder = passwordEncoder;
    }
    public Page<UsuarioResponse> listar(Pageable pageable) { return repository.findAllBy(pageable).map(this::toResponse); }
    public UsuarioResponse obtener(Long id) { return toResponse(buscar(id)); }
    public UsuarioResponse obtenerPorUsername(String username) {
        return repository.findWithCiudadByUsernameIgnoreCase(username).map(this::toResponse).orElseThrow(this::notFound);
    }
    @Transactional public UsuarioResponse crear(UsuarioCreateRequest r) {
        return crear(r, false);
    }
    @Transactional public UsuarioResponse crear(UsuarioCreateRequest r, boolean admin) {
        String username = r.username().trim();
        String email = r.email().trim();
        Usuario.Rol rol = admin && r.rol() != null ? r.rol() : Usuario.Rol.CLIENTE;
        validarUnicos(username, email, null);
        validarTipoCliente(rol, r.tipoCliente(), r.apellidos());
        Usuario u = new Usuario(); u.setNombre(r.nombre()); u.setApellidos(r.apellidos()); u.setUsername(r.username().trim());
        u.setEmail(email); u.setPasswordHash(passwordEncoder.encode(r.password()));
        u.setRol(rol); u.setTipoCliente(r.tipoCliente());
        u.setCiudad(ciudad(r.ciudadId())); u.setTelefono(r.telefono()); return toResponse(repository.save(u));
    }
    @Transactional public UsuarioResponse actualizar(Long id, UsuarioUpdateRequest r) {
        Usuario u = buscar(id); validarUnicos(r.username(), r.email(), id);
        validarTipoCliente(r.rol() == null ? u.getRol() : r.rol(), r.tipoCliente(), r.apellidos());
        u.setNombre(r.nombre()); u.setApellidos(r.apellidos()); u.setUsername(r.username().trim()); u.setEmail(r.email().trim());
        if (r.rol() != null) u.setRol(r.rol()); u.setTipoCliente(r.tipoCliente()); u.setCiudad(ciudad(r.ciudadId()));
        u.setTelefono(r.telefono()); if (r.estado() != null) u.setEstado(r.estado()); return toResponse(repository.save(u));
    }
    @Transactional public UsuarioResponse actualizarMe(String name, UsuarioMeUpdateRequest r) {
        Usuario u = buscarPorUsername(name); validarUnicos(u.getUsername(), r.email(), u.getUsuarioId());
        validarTipoCliente(u.getRol(), r.tipoCliente(), r.apellidos());
        u.setNombre(r.nombre()); u.setApellidos(r.apellidos()); u.setEmail(r.email().trim());
        u.setTipoCliente(r.tipoCliente()); u.setCiudad(ciudad(r.ciudadId())); u.setTelefono(r.telefono());
        return toResponse(repository.save(u));
    }
    @Transactional public void cambiarPassword(Long id, CambioPasswordRequest r) { cambiarPassword(buscar(id), r); }
    @Transactional public void cambiarMiPassword(String name, CambioPasswordRequest r) { cambiarPassword(buscarPorUsername(name), r); }
    private void cambiarPassword(Usuario u, CambioPasswordRequest r) {
        if (!passwordEncoder.matches(r.passwordActual(), u.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es válida");
        u.setPasswordHash(passwordEncoder.encode(r.passwordNueva())); repository.save(u);
    }
    @Transactional public void cambiarBloqueo(Long id, boolean bloqueado) {
        Usuario u = buscar(id); u.setBloqueadoHasta(bloqueado ? OffsetDateTime.now().plusYears(100) : null);
        if (!bloqueado) u.setIntentosFallidos((short) 0); repository.save(u);
    }
    @Transactional public void eliminar(Long id) { if (!repository.existsById(id)) throw notFound(); repository.deleteById(id); }
    private Usuario buscar(Long id) { return repository.findWithCiudadByUsuarioId(id).orElseThrow(this::notFound); }
    private Usuario buscarPorUsername(String name) { return repository.findWithCiudadByUsernameIgnoreCase(name).orElseThrow(this::notFound); }
    private Ciudad ciudad(Long id) { return id == null ? null : ciudadRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada")); }
    private void validarUnicos(String username, String email, Long id) {
        username = username.trim(); email = email.trim();
        boolean un = id == null ? repository.existsByUsernameIgnoreCase(username) : repository.existsByUsernameIgnoreCaseAndUsuarioIdNot(username, id);
        boolean em = id == null ? repository.existsByEmailIgnoreCase(email) : repository.existsByEmailIgnoreCaseAndUsuarioIdNot(email, id);
        if (un || em) throw new ResponseStatusException(HttpStatus.CONFLICT, un ? "El username ya está registrado" : "El email ya está registrado");
    }
    private void validarTipoCliente(Usuario.Rol rol, Usuario.TipoCliente tipo, String apellidos) {
        if (rol == Usuario.Rol.CLIENTE && tipo == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de cliente es obligatorio para el rol CLIENTE");
        if (tipo == Usuario.TipoCliente.PERSONA && (apellidos == null || apellidos.isBlank()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los apellidos son obligatorios para una persona");
    }
    private ResponseStatusException notFound() { return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"); }
    private UsuarioResponse toResponse(Usuario u) {
        UsuarioResponse.CiudadResponse c = u.getCiudad() == null ? null : new UsuarioResponse.CiudadResponse(u.getCiudad().getCiudadId(), u.getCiudad().getNombre());
        return new UsuarioResponse(u.getUsuarioId(), u.getNombre(), u.getApellidos(), u.getUsername(), u.getEmail(),
                u.getRol(), u.getTipoCliente(), c, u.getTelefono(), u.getEstado(), u.getIntentosFallidos(), u.getBloqueadoHasta(), u.getFechaRegistro());
    }
}

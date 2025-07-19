package com.pigtrack.pigtrack_api.api.controllers;

import com.pigtrack.pigtrack_api.api.controllers.dto.UsuarioRequestDTO;
import com.pigtrack.pigtrack_api.application.usecases.*;
import com.pigtrack.pigtrack_api.domain.models.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final RegistrarUsuario registrarUsuario;
    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final ListarUsuarios listarUsuarios;
    private final EliminarUsuario eliminarUsuario;
    private final ActualizarUsuario actualizarUsuario;

    public UsuarioController(
            RegistrarUsuario registrarUsuario,
            BuscarUsuarioPorId buscarUsuarioPorId,
            ListarUsuarios listarUsuarios,
            EliminarUsuario eliminarUsuario,
            ActualizarUsuario actualizarUsuario
    ) {
        this.registrarUsuario = registrarUsuario;
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.listarUsuarios = listarUsuarios;
        this.eliminarUsuario = eliminarUsuario;
        this.actualizarUsuario = actualizarUsuario;
    }

    @PostMapping
    public ResponseEntity<String> registrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        try {
            UUID idGenerado = UUID.randomUUID();
            Usuario usuario = new Usuario(
                    idGenerado,
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getEmail(),
                    dto.getTelefono(),
                    dto.getContraseña(),
                    dto.getRol()
            );

            registrarUsuario.ejecutar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con ID: " + idGenerado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        Optional<Usuario> usuario = buscarUsuarioPorId.ejecutar(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = listarUsuarios.ejecutar();
        return ResponseEntity.ok(usuarios);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable UUID id) {
        boolean eliminado = eliminarUsuario.ejecutar(id);

        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioRequestDTO dto
    ) {
        Usuario actualizado = new Usuario(
                id,
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getTelefono(),
                dto.getContraseña(),
                dto.getRol()
        );

        boolean fueActualizado = actualizarUsuario.ejecutar(id, actualizado);

        if (fueActualizado) {
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}

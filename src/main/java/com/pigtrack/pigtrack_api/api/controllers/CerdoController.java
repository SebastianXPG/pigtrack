package com.pigtrack.pigtrack_api.api.controllers;

import com.pigtrack.pigtrack_api.api.controllers.dto.CerdoRequestDTO;
import com.pigtrack.pigtrack_api.application.usecases.*;
import com.pigtrack.pigtrack_api.domain.models.Cerdo;
import com.pigtrack.pigtrack_api.domain.models.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/cerdos")
public class CerdoController {

    private final RegistrarCerdo registrarCerdo;
    private final ListarCerdos listarCerdos;
    private final BuscarCerdoPorId buscarCerdoPorId;
    private final EliminarCerdo eliminarCerdo;
    private final ActualizarCerdo actualizarCerdo;

    public CerdoController(
            RegistrarCerdo registrarCerdo,
            BuscarCerdoPorId buscarCerdoPorId,
            ListarCerdos listarCerdos,
            EliminarCerdo eliminarCerdo,
            ActualizarCerdo actualizarCerdo) {
        this.registrarCerdo = registrarCerdo;
        this.buscarCerdoPorId = buscarCerdoPorId;
        this.listarCerdos = listarCerdos;
        this.eliminarCerdo = eliminarCerdo;
        this.actualizarCerdo = actualizarCerdo;
    }

    @PostMapping
    public ResponseEntity<String> registrar(@Valid @RequestBody CerdoRequestDTO dto) {
        if (!getRolActual().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden registrar cerdos");
        }

        try {
            Cerdo cerdo = new Cerdo(
                    UUID.randomUUID(),
                    dto.getNombre(),
                    dto.getRaza(),
                    dto.getFechaNacimiento(),
                    dto.getPeso(),
                    dto.getUsuarioId()
            );

            registrarCerdo.ejecutar(cerdo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cerdo registrado exitosamente");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Cerdo>> listarTodos() {
        if (getRolActual().equalsIgnoreCase("CLIENTE")) {
            return ResponseEntity.ok(listarCerdos.ejecutarPorUsuario(getUsuarioIdActual()));
        }

        return ResponseEntity.ok(listarCerdos.ejecutar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        Optional<Cerdo> cerdo = buscarCerdoPorId.ejecutar(id);

        if (cerdo.isPresent()) {
            return ResponseEntity.ok(cerdo.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cerdo no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable UUID id, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        if (!usuario.getRol().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo los administradores pueden eliminar cerdos");
        }

        eliminarCerdo.ejecutar(id);
        return ResponseEntity.ok("Cerdo eliminado correctamente");
    }



    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable UUID id, @Valid @RequestBody CerdoRequestDTO dto) {
        try {
            Cerdo cerdo = new Cerdo(
                    id,
                    dto.getNombre(),
                    dto.getRaza(),
                    dto.getFechaNacimiento(),
                    dto.getPeso(),
                    dto.getUsuarioId()
            );

            actualizarCerdo.ejecutar(cerdo);
            return ResponseEntity.ok("Cerdo actualizado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el cerdo: " + e.getMessage());
        }
    }

    private String getRolActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
    }

    private UUID getUsuarioIdActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            return UUID.fromString(userDetails.getUsername());
        }
        return null;
    }
}

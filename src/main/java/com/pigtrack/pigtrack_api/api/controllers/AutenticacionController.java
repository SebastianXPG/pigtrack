package com.pigtrack.pigtrack_api.api.controllers;

import com.pigtrack.pigtrack_api.api.controllers.dto.AutenticacionRequestDTO;
import com.pigtrack.pigtrack_api.application.usecases.AutenticarUsuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final AutenticarUsuario autenticarUsuario;

    public AutenticacionController(AutenticarUsuario autenticarUsuario) {
        this.autenticarUsuario = autenticarUsuario;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AutenticacionRequestDTO dto) {
        try {
            String token = autenticarUsuario.ejecutar(dto.getEmail(), dto.getContraseña());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}

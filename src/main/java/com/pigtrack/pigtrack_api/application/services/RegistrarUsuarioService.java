package com.pigtrack.pigtrack_api.application.services;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegistrarUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(Usuario usuario) {
        // Si el usuario no tiene ID, lo generamos aquí
        if (usuario.getId() == null) {
            usuario.setId(UUID.randomUUID());
        }

        // (Opcional) Validar si ya existe un email registrado
        usuarioRepository.buscarPorEmail(usuario.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("El email ya está registrado.");
        });

        return usuarioRepository.guardar(usuario);
    }
}

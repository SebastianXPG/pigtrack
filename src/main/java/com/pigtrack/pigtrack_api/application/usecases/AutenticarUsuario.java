package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import com.pigtrack.pigtrack_api.infrastructure.security.JwtService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticarUsuario {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AutenticarUsuario(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public String ejecutar(String email, String contraseña) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getContraseña().equals(contraseña)) {
            throw new IllegalArgumentException("Email o contraseña incorrectos");
        }

        // Si todo está bien, generar y retornar token
        return jwtService.generarToken(usuario);
    }
}

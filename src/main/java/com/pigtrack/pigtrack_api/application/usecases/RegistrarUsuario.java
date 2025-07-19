package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrarUsuario {

    private final UsuarioRepository usuarioRepository;

    public RegistrarUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void ejecutar(Usuario usuario) {
        // Validar que no exista un usuario con el mismo email
        boolean emailYaExiste = usuarioRepository.buscarPorEmail(usuario.getEmail()).isPresent();
        if (emailYaExiste) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese email");
        }

        usuarioRepository.guardar(usuario);
    }
}

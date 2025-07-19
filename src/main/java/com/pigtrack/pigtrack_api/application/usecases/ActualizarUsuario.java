package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActualizarUsuario {

    private final UsuarioRepository repository;

    public ActualizarUsuario(UsuarioRepository repository) {
        this.repository = repository;
    }

    public boolean ejecutar(UUID id, Usuario datosActualizados) {
        Optional<Usuario> existente = repository.buscarPorId(id);

        if (existente.isPresent()) {
            Usuario actualizado = new Usuario(
                    id,
                    datosActualizados.getNombre(),
                    datosActualizados.getApellido(),
                    datosActualizados.getEmail(),
                    datosActualizados.getTelefono(),
                    datosActualizados.getContraseña(),
                    datosActualizados.getRol()
            );

            repository.guardar(actualizado);
            return true;
        } else {
            return false;
        }
    }
}

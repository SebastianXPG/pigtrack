package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EliminarUsuario {

    private final UsuarioRepository repository;

    public EliminarUsuario(UsuarioRepository repository) {
        this.repository = repository;
    }

    public boolean ejecutar(UUID id) {
        if (repository.buscarPorId(id).isPresent()) {
            repository.eliminar(id);
            return true;
        }
        return false;
    }
}

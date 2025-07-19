package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarUsuarioPorId {

    private final UsuarioRepository repository;

    public BuscarUsuarioPorId(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Optional<Usuario> ejecutar(UUID id) {
        return repository.buscarPorId(id);
    }
}

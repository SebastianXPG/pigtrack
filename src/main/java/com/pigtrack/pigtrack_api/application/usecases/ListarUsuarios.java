package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarUsuarios {

    private final UsuarioRepository repository;

    public ListarUsuarios(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> ejecutar() {
        return repository.buscarTodos();
    }
}

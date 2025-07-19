package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Cerdo;
import com.pigtrack.pigtrack_api.domain.repositories.CerdoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarCerdos {

    private final CerdoRepository repository;

    public ListarCerdos(CerdoRepository repository) {
        this.repository = repository;
    }

    public List<Cerdo> ejecutar() {
        return repository.listarTodos();
    }

    public List<Cerdo> ejecutarPorUsuario(UUID usuarioId) {
        return repository.listarPorUsuario(usuarioId);
    }
}

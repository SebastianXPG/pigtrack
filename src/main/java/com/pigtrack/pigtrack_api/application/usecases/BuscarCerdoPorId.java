package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Cerdo;
import com.pigtrack.pigtrack_api.domain.repositories.CerdoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BuscarCerdoPorId {

    private final CerdoRepository repository;

    public BuscarCerdoPorId(CerdoRepository repository) {
        this.repository = repository;
    }

    public Optional<Cerdo> ejecutar(UUID id) {
        return repository.buscarPorId(id);
    }
}

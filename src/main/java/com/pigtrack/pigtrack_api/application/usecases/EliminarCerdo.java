package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.repositories.CerdoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EliminarCerdo {

    private final CerdoRepository repository;

    public EliminarCerdo(CerdoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(UUID id) {
        repository.eliminar(id);
    }
}

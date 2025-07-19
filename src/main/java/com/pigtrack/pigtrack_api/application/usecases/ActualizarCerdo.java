package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Cerdo;
import com.pigtrack.pigtrack_api.domain.repositories.CerdoRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualizarCerdo {

    private final CerdoRepository repository;

    public ActualizarCerdo(CerdoRepository repository) {
        this.repository = repository;
    }

    public Cerdo ejecutar(Cerdo cerdo) {
        return repository.actualizar(cerdo);
    }
}

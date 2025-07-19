package com.pigtrack.pigtrack_api.application.usecases;

import com.pigtrack.pigtrack_api.domain.models.Cerdo;
import com.pigtrack.pigtrack_api.domain.repositories.CerdoRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrarCerdo {

    private final CerdoRepository repository;

    public RegistrarCerdo(CerdoRepository repository) {
        this.repository = repository;
    }

    public void ejecutar(Cerdo cerdo) {
        repository.guardar(cerdo);
    }
}

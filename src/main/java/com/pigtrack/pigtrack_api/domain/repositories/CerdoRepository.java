package com.pigtrack.pigtrack_api.domain.repositories;

import com.pigtrack.pigtrack_api.domain.models.Cerdo;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface CerdoRepository {
    Cerdo guardar(Cerdo cerdo);
    Optional<Cerdo> buscarPorId(UUID id);
    List<Cerdo> listarTodos();
    List<Cerdo> listarPorUsuario(UUID usuarioId); // <-- usa este único nombre
    void eliminar(UUID id);
    Cerdo actualizar(Cerdo cerdo);
}

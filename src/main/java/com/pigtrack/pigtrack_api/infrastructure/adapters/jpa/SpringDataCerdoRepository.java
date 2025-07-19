package com.pigtrack.pigtrack_api.infrastructure.adapters.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCerdoRepository extends JpaRepository<CerdoEntity, UUID> {

    // Buscar todos los cerdos asociados a un usuario específico
    List<CerdoEntity> findByUsuarioId(UUID usuarioId);
}

package com.pigtrack.pigtrack_api.infrastructure.adapters.jpa;

import com.pigtrack.pigtrack_api.domain.models.Cerdo;
import com.pigtrack.pigtrack_api.domain.repositories.CerdoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class JpaCerdoRepository implements CerdoRepository {

    private final SpringDataCerdoRepository jpa;

    public JpaCerdoRepository(SpringDataCerdoRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Cerdo guardar(Cerdo cerdo) {
        CerdoEntity entity = mapToEntity(cerdo);
        CerdoEntity guardado = jpa.save(entity);
        return mapToDomain(guardado);
    }

    @Override
    public Optional<Cerdo> buscarPorId(UUID id) {
        return jpa.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Cerdo> listarTodos() {
        return jpa.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cerdo> listarPorUsuario(UUID usuarioId) {
        return jpa.findByUsuarioId(usuarioId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }



    @Override
    public void eliminar(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Cerdo actualizar(Cerdo cerdo) {
        return guardar(cerdo);
    }

    // --- Mapeo ---

    private CerdoEntity mapToEntity(Cerdo cerdo) {
        CerdoEntity entity = new CerdoEntity();
        entity.setId(cerdo.getId());
        entity.setNombre(cerdo.getNombre());
        entity.setRaza(cerdo.getRaza());
        entity.setFechaNacimiento(cerdo.getFechaNacimiento());
        entity.setPeso(cerdo.getPeso());
        entity.setUsuarioId(cerdo.getUsuarioId());
        return entity;
    }

    private Cerdo mapToDomain(CerdoEntity entity) {
        return new Cerdo(
                entity.getId(),
                entity.getNombre(),
                entity.getRaza(),
                entity.getFechaNacimiento(),
                entity.getPeso(),
                entity.getUsuarioId()
        );
    }
}

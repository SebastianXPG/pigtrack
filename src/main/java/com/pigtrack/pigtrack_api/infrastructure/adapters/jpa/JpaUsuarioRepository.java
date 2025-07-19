package com.pigtrack.pigtrack_api.infrastructure.adapters.jpa;

import com.pigtrack.pigtrack_api.domain.models.Usuario;
import com.pigtrack.pigtrack_api.domain.repositories.UsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUsuarioRepository implements UsuarioRepository {

    private final SpringDataUsuarioRepository jpa;

    public JpaUsuarioRepository(SpringDataUsuarioRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = mapToEntity(usuario);
        UsuarioEntity saved = jpa.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return jpa.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpa.findByEmail(email).map(this::mapToDomain);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return jpa.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public void eliminar(UUID id) {
        jpa.deleteById(id);
    }



    // --- Métodos de mapeo ---
    private UsuarioEntity mapToEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setApellido(usuario.getApellido());
        entity.setEmail(usuario.getEmail());
        entity.setTelefono(usuario.getTelefono());
        entity.setContraseña(usuario.getContraseña());
        entity.setRol(usuario.getRol());
        return entity;
    }

    private Usuario mapToDomain(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getEmail(),
                entity.getTelefono(),
                entity.getContraseña(),
                entity.getRol()
        );
    }
}

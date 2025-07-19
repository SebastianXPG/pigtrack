package com.pigtrack.pigtrack_api.domain.repositories;

import com.pigtrack.pigtrack_api.domain.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    void eliminar(UUID id);

}

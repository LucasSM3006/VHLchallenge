package com.vhl.library.repos;

import com.vhl.library.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}

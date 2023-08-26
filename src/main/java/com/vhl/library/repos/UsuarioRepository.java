package com.vhl.library.repos;

import com.vhl.library.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    List<Usuario> findByNomeContainingIgnoreCaseAndExcluidoIsFalse(String nome);

    Optional<Usuario> findByIdAndExcluidoFalse(Integer id);
}

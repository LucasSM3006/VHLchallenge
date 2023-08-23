package com.vhl.library.repos;

import com.vhl.library.model.UsuarioLivro;
import com.vhl.library.model.UsuarioLivroId;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioLivroRepository extends CrudRepository<UsuarioLivro, UsuarioLivroId> {
}

package com.vhl.library.repos;

import com.vhl.library.model.UsuarioLivro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioLivroRepository extends CrudRepository<UsuarioLivro, Integer> {

    @Query("SELECT ul FROM UsuarioLivro ul WHERE ul.usuario.id = :usuarioId AND ul.livro.id = :livroId AND ul.ativo = true")
    List<UsuarioLivro> findActiveUsuarioLivrosByUsuarioIdAndLivroId(
            @Param("usuarioId") Integer usuarioId,
            @Param("livroId") Integer livroId
    );

    List<UsuarioLivro> findByAtivoTrueAndUsuarioId(@Param("usuarioId") Integer usuarioId);
}

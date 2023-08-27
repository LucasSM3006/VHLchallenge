package com.vhl.library.repos;

import com.vhl.library.model.UsuarioLivro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioLivroRepository extends CrudRepository<UsuarioLivro, Integer> {

    Optional<UsuarioLivro> findByAtivoTrueAndLivroId(@Param("livro_id") Integer livroId);

    @Query("SELECT ul FROM UsuarioLivro ul WHERE ul.usuario.id = :usuarioId AND ul.livro.id = :livroId AND ul.ativo = true")
    Optional<UsuarioLivro> findActiveUsuarioLivrosByUsuarioIdAndLivroId(
            @Param("usuarioId") Integer usuarioId,
            @Param("livroId") Integer livroId
    );

    List<UsuarioLivro> findByAtivoTrueAndUsuarioId(@Param("usuarioId") Integer usuarioId);
}

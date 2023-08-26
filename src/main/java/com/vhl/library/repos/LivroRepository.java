package com.vhl.library.repos;

import com.vhl.library.model.Livro;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends CrudRepository<Livro, Integer> {
    List<Livro> findByAutorContainingIgnoreCaseAndExcluidoIsFalse(String autor);
    List<Livro> findByTituloContainingIgnoreCaseAndExcluidoIsFalse(String titulo);
    List<Livro> findByAutorContainingIgnoreCaseAndTituloContainingIgnoreCaseAndExcluidoIsFalse(String autor, String titulo);

    Optional<Livro> findByIdAndExcluidoFalse(Integer id);
}

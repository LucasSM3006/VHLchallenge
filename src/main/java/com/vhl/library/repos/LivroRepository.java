package com.vhl.library.repos;

import com.vhl.library.model.Livro;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LivroRepository extends CrudRepository<Livro, Integer> {
    List<Livro> findByAutorContainingIgnoreCase(String autor);
    List<Livro> findByTituloContainingIgnoreCase(String titulo);
    List<Livro> findByAutorContainingIgnoreCaseAndTituloContainingIgnoreCase(String autor, String titulo);
}

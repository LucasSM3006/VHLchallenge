package com.vhl.library.repos;

import com.vhl.library.model.Livro;
import org.springframework.data.repository.CrudRepository;

public interface LivroRepository extends CrudRepository<Livro, Integer> {
}

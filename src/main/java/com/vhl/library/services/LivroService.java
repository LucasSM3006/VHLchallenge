package com.vhl.library.services;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.repos.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public ResponseEntity<String> adicionar(LivroDTO livroDTO) {
        return new ResponseEntity<>(livroDTO.getTitulo(), HttpStatus.OK);
    }

    public ResponseEntity<String> editar(int id, LivroDTO livroDTO) {
        return new ResponseEntity<>(livroDTO.getTitulo(), HttpStatus.OK);
    }

    public ResponseEntity<String> excluir(int id) {
        livroRepository.deleteById(id);
        return new ResponseEntity<>("Ok.", HttpStatus.OK);
    }
}

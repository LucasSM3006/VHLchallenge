package com.vhl.library.controller;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.services.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionar(@RequestBody LivroDTO livroDTO) {
        return livroService.adicionar(livroDTO);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editar(@PathVariable("id") int id, @RequestBody LivroDTO livroDTO) {
        return livroService.editar(id, livroDTO);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluir(@PathVariable("id") int id) {
        return livroService.excluir(id);
    }

    @GetMapping("/emprestar")
    public String emprestar() {
        return "";
    }

    @GetMapping("/devolver")
    public String devolver() {
        return "";
    }

    @GetMapping("/pesquisarPorTitulo")
    public String pesquisarPorTitulo() {
        return "";
    }

    @GetMapping("/pesquisarPorAutor")
    public String pesquisarPorAutor() {
        return "";
    }
}

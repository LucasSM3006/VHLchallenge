package com.vhl.library.controller;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.services.LivroService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/adicionar")
    public String adicionar(@RequestBody LivroDTO livroDTO) {
        // return livroService.adicionar(livroDTO);
        return "tudo ok";
    }

    @GetMapping("/editar")
    public String editar() {
        // livroService.editar();
        return "";
    }

    @GetMapping("/excluir")
    public String excluir() {
        // livroService.excluir(id);
        return "";
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

package com.vhl.library.controller;

import com.vhl.library.model.DTO.UsuarioDTO;
import com.vhl.library.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/adicionar")
    public ResponseEntity<String> adicionar(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.adicionar(usuarioDTO);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<String> editar(@PathVariable("id") int id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.editar(id, usuarioDTO);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluir(@PathVariable("id") int id) {
        return usuarioService.excluir(id);
    }

    @GetMapping("/pesquisarPorNome")
    public ResponseEntity<List<UsuarioDTO>> pesquisarPorNome(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.pesquisarPorNome(usuarioDTO);
    }
}

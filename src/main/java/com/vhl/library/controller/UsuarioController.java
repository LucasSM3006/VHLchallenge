package com.vhl.library.controller;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.model.DTO.UsuarioDTO;
import com.vhl.library.model.Usuario;
import com.vhl.library.services.LivroService;
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

    @GetMapping("/adicionar")
    public String adicionar(@RequestBody UsuarioDTO usuarioDTO) {
        // return usuarioService.adicionar(usuarioDTO);
        return "tudo ok";
    }

    @GetMapping("/editar")
    public String editar() {
        // return usuarioService.editar();
        return "";
    }

    @GetMapping("/excluir")
    public String excluir() {
        // return usuarioService.excluir(id);
        return "";
    }

    @GetMapping("/pesquisarPorNome")
    public ResponseEntity<List<UsuarioDTO>> pesquisarPorNome(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.pesquisarPorNome(usuarioDTO);
    }
}

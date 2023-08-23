package com.vhl.library.services;

import com.vhl.library.model.DTO.UsuarioDTO;
import com.vhl.library.model.Usuario;
import com.vhl.library.repos.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<UsuarioDTO> adicionar(UsuarioDTO usuarioDTO) {
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    public String editar() {
        return null;
    }

    public String excluir() {
        return null;
    }

    public ResponseEntity<List<Usuario>> listar(UsuarioDTO usuarioDTO) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(usuarioDTO.getNome());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}

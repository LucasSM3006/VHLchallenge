package com.vhl.library.services;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.model.DTO.UsuarioDTO;
import com.vhl.library.model.Livro;
import com.vhl.library.model.Usuario;
import com.vhl.library.repos.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<String> adicionar(UsuarioDTO usuarioDTO) {
        String mensagem = "";
        String nome = usuarioDTO.getNome().trim();

        if (nome.isBlank() || !nome.matches("[a-zA-Z_ ]+")) {
            mensagem = "Favor utilizar somente letras.";
        } else {
            Usuario novoUsuario = new Usuario();

            novoUsuario.setNome(usuarioDTO.getNome());

            usuarioRepository.save(novoUsuario);

            mensagem = "Usuário salvo.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public String editar() {
        return null;
    }

    public String excluir() {
        return null;
    }

    public ResponseEntity<List<UsuarioDTO>> pesquisarPorNome(UsuarioDTO usuarioDTO) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(usuarioDTO.getNome());
        List<UsuarioDTO> usuariosDTO = convertToDTO(usuarios);

        if(usuarios.isEmpty()) {
            UsuarioDTO usuarioVazio = new UsuarioDTO();
            usuarioVazio.setId(0L);
            usuarioVazio.setNome("Sem resultados.");
            usuariosDTO.add(usuarioVazio);
        }

        return new ResponseEntity<>(usuariosDTO, HttpStatus.OK);
    }

    private List<UsuarioDTO> convertToDTO(List<Usuario> usuarios) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();

        if(usuarios.isEmpty()) return usuariosDTO;

        for(Usuario usuario : usuarios) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setId(usuario.getId());
            usuarioDTO.setNome(usuario.getNome());

            usuariosDTO.add(usuarioDTO);
        }

        return usuariosDTO;
    }
}

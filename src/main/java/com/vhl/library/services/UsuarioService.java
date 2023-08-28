package com.vhl.library.services;

import com.vhl.library.model.DTO.UsuarioDTO;
import com.vhl.library.model.Usuario;
import com.vhl.library.model.UsuarioLivro;
import com.vhl.library.repos.UsuarioLivroRepository;
import com.vhl.library.repos.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioLivroRepository usuarioLivroRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioLivroRepository usuarioLivroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioLivroRepository = usuarioLivroRepository;
    }

    public ResponseEntity<String> adicionar(UsuarioDTO usuarioDTO) {
        String mensagem;

        usuarioDTO.setNome(usuarioDTO.getNome() == null ? " " : usuarioDTO.getNome());

        String nome = usuarioDTO.getNome().trim();

        if (nome.isBlank() || !nome.matches("[a-zA-Z_ ]+")) {
            mensagem = "Favor utilizar somente letras.";
        } else {
            Usuario novoUsuario = new Usuario();

            novoUsuario.setNome(usuarioDTO.getNome());
            novoUsuario.setExcluido(false);

            usuarioRepository.save(novoUsuario);

            mensagem = "Usuário salvo.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<String> editar(int id, UsuarioDTO usuarioDTO) {
        String mensagem;

        usuarioDTO.setNome(usuarioDTO.getNome() == null ? " " : usuarioDTO.getNome());

        String nome = usuarioDTO.getNome().trim();

        if (nome.isBlank() || !nome.matches("[a-zA-Z_ ]+")) {
            mensagem = "Favor utilizar somente letras.";
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        }

        Optional<Usuario> usuario = usuarioRepository.findByIdAndExcluidoFalse(id);

        if(usuario.isPresent()) {
            Usuario usuarioExistente = usuario.get();
            String nomeAnterior = usuarioExistente.getNome();

            usuarioExistente.setNome(usuarioDTO.getNome());

            usuarioRepository.save(usuarioExistente);

            mensagem = "Usuário editado. Nome anterior: " + nomeAnterior + "; Novo nome: " + usuarioExistente.getNome();
        } else {
            mensagem = "Usuário não existente ou excluído.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<String> excluir(int id) {
        String mensagem;

        Optional<Usuario> usuario = usuarioRepository.findByIdAndExcluidoFalse(id);

        if(usuario.isEmpty()) {
            mensagem = "Usuário não encontrado.";
        } else {
            List<UsuarioLivro> usuarioLivros = usuarioLivroRepository.findByAtivoTrueAndUsuarioId(id);

            if(usuarioLivros.isEmpty()) {
                Usuario usuarioExistente = usuario.get();

                usuarioExistente.setExcluido(true);

                usuarioRepository.save(usuarioExistente);

                mensagem = "Usuário excluído.";

            } else {
                mensagem = "Usuário tem livros pendentes. Devolver primeiro antes de excluir.";
            }
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<List<UsuarioDTO>> pesquisarPorNome(UsuarioDTO usuarioDTO) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCaseAndExcluidoIsFalse(usuarioDTO.getNome());
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

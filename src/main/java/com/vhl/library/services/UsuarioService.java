package com.vhl.library.services;

import com.vhl.library.model.DTO.UsuarioDTO;
import com.vhl.library.model.Usuario;
import com.vhl.library.model.UsuarioLivro;
import com.vhl.library.repos.UsuarioLivroRepository;
import com.vhl.library.repos.UsuarioRepository;
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
        String nome = usuarioDTO.getNome().trim();

        if (!isNomeValido(nome)) {
            return ResponseEntity.ok("Favor utilizar somente letras.");
        }

        Usuario novoUsuario = createUsuarioWithNome(nome);
        usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário salvo.");
    }

    public ResponseEntity<String> editar(int id, UsuarioDTO usuarioDTO) {
        String nome = usuarioDTO.getNome().trim();

        if (!isNomeValido(nome)) {
            return ResponseEntity.ok("Favor utilizar somente letras.");
        }

        Optional<Usuario> usuario = usuarioRepository.findByIdAndExcluidoFalse(id);

        if (usuario.isPresent()) {
            Usuario usuarioExistente = usuario.get();
            String nomeAnterior = usuarioExistente.getNome();

            usuarioExistente.setNome(nome);
            usuarioRepository.save(usuarioExistente);

            return ResponseEntity.ok("Usuário editado. Nome anterior: " + nomeAnterior + "; Novo nome: " + usuarioExistente.getNome());
        }

        return ResponseEntity.ok("Usuário não existente ou excluído.");
    }

    public ResponseEntity<String> excluir(int id) {
        Optional<Usuario> usuario = usuarioRepository.findByIdAndExcluidoFalse(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.ok("Usuário não encontrado.");
        }

        List<UsuarioLivro> usuarioLivros = usuarioLivroRepository.findByAtivoTrueAndUsuarioId(id);

        if (!usuarioLivros.isEmpty()) {
            return ResponseEntity.ok("Usuário tem livros pendentes. Devolver primeiro antes de excluir.");
        }

        Usuario usuarioExistente = usuario.get();
        usuarioExistente.setExcluido(true);
        usuarioRepository.save(usuarioExistente);

        return ResponseEntity.ok("Usuário excluído.");
    }

    public ResponseEntity<List<UsuarioDTO>> pesquisarPorNome(UsuarioDTO usuarioDTO) {
        List<Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCaseAndExcluidoIsFalse(usuarioDTO.getNome());
        List<UsuarioDTO> usuariosDTO = convertToDTO(usuarios);

        if (usuarios.isEmpty()) {
            usuariosDTO.add(createEmptyUsuarioDTO());
        }

        return ResponseEntity.ok(usuariosDTO);
    }

    private boolean isNomeValido(String nome) {
        return !nome.isBlank() && nome.matches("[a-zA-Z_ ]+");
    }

    private Usuario createUsuarioWithNome(String nome) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(nome);
        novoUsuario.setExcluido(false);
        return novoUsuario;
    }

    private UsuarioDTO createEmptyUsuarioDTO() {
        UsuarioDTO usuarioVazio = new UsuarioDTO();
        usuarioVazio.setId(0L);
        usuarioVazio.setNome("Sem resultados.");
        return usuarioVazio;
    }

    private List<UsuarioDTO> convertToDTO(List<Usuario> usuarios) {
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();

        if (!usuarios.isEmpty()) {
            for (Usuario usuario : usuarios) {
                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setId(usuario.getId());
                usuarioDTO.setNome(usuario.getNome());
                usuariosDTO.add(usuarioDTO);
            }
        }

        return usuariosDTO;
    }
}

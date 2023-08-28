package com.vhl.library.services;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.model.DTO.LivroPesquisaDTO;
import com.vhl.library.model.DTO.UsuarioLivroDTO;
import com.vhl.library.model.Livro;
import com.vhl.library.model.Usuario;
import com.vhl.library.model.UsuarioLivro;
import com.vhl.library.repos.LivroRepository;
import com.vhl.library.repos.UsuarioLivroRepository;
import com.vhl.library.repos.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioLivroRepository usuarioLivroRepository;

    public LivroService(LivroRepository livroRepository, UsuarioRepository usuarioRepository, UsuarioLivroRepository usuarioLivroRepository) {
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
        this.usuarioLivroRepository = usuarioLivroRepository;
    }

    public ResponseEntity<String> adicionar(LivroDTO livroDTO) {
        if (livroDTO.getTitulo().isBlank() || livroDTO.getAutor().isBlank()) {
            return ResponseEntity.ok("Informações faltando. Autor e Título são obrigatórios.");
        }

        Livro novoLivro = new Livro();
        novoLivro.setAutor(livroDTO.getAutor().trim());
        novoLivro.setTitulo(livroDTO.getTitulo().trim());
        novoLivro.setExcluido(false);

        livroRepository.save(novoLivro);
        return ResponseEntity.ok("Livro salvo.");
    }

    public ResponseEntity<List<LivroPesquisaDTO>> pesquisarPorAutor(LivroDTO livroDTO) {
        return processarLivros(livroRepository.findByAutorContainingIgnoreCaseAndExcluidoIsFalse(livroDTO.getAutor()));
    }

    public ResponseEntity<List<LivroPesquisaDTO>> pesquisarPorTitulo(LivroDTO livroDTO) {
        return processarLivros(livroRepository.findByTituloContainingIgnoreCaseAndExcluidoIsFalse(livroDTO.getTitulo()));
    }

    public ResponseEntity<List<LivroPesquisaDTO>> pesquisarPorTituloEAutor(LivroDTO livroDTO) {
        return processarLivros(livroRepository.findByAutorContainingIgnoreCaseAndTituloContainingIgnoreCaseAndExcluidoIsFalse(livroDTO.getAutor(), livroDTO.getTitulo()));
    }

    private ResponseEntity<List<LivroPesquisaDTO>> processarLivros(List<Livro> livros) {
        List<LivroPesquisaDTO> livrosDTO = convertToDTO(livros);
        return isEmpty(livrosDTO);
    }

    public ResponseEntity<String> editar(int id, LivroDTO livroDTO) {
        Optional<Livro> livro = livroRepository.findByIdAndExcluidoFalse(id);

        if (livro.isEmpty()) {
            return ResponseEntity.ok("Livro não existente.");
        }

        Livro livroExistente = livro.get();
        String autorAnterior = livroExistente.getAutor();
        String tituloAnterior = livroExistente.getTitulo();

        livroExistente.setTitulo(livroDTO.getTitulo().isBlank() ? tituloAnterior : livroDTO.getTitulo().trim());
        livroExistente.setAutor(livroDTO.getAutor().isBlank() ? autorAnterior : livroDTO.getAutor().trim());

        if (autorAnterior.equals(livroExistente.getAutor()) && tituloAnterior.equals(livroExistente.getTitulo())) {
            return ResponseEntity.ok("Informações iguais, nada foi mudado.");
        }

        livroRepository.save(livroExistente);
        return ResponseEntity.ok("Livro editado.");
    }

    public ResponseEntity<String> excluir(int id) {
        Optional<Livro> livro = livroRepository.findByIdAndExcluidoFalse(id);

        if (livro.isEmpty()) {
            return ResponseEntity.ok("Livro não encontrado.");
        }

        Optional<UsuarioLivro> usuarioLivros = usuarioLivroRepository.findByAtivoTrueAndLivroId(id);

        if (usuarioLivros.isPresent()) {
            return ResponseEntity.ok("Livro emprestado para um usuário, não pode ser excluído.");
        }

        Livro livroExistente = livro.get();
        livroExistente.setExcluido(true);
        livroRepository.save(livroExistente);

        return ResponseEntity.ok("Livro excluído.");
    }

    public ResponseEntity<String> emprestar(UsuarioLivroDTO usuarioLivroDTO) {
        if (usuarioLivroDTO.getLivro_id() <= 0 || usuarioLivroDTO.getUsuario_id() <= 0) {
            return ResponseEntity.ok("É necessário o ID do livro e o ID do usuário. Zero é inválido.");
        }

        Optional<UsuarioLivro> registroExistente = usuarioLivroRepository.findActiveUsuarioLivrosByUsuarioIdAndLivroId(usuarioLivroDTO.getUsuario_id(), usuarioLivroDTO.getLivro_id());

        if (registroExistente.isEmpty()) {
            Optional<Usuario> usuario = usuarioRepository.findByIdAndExcluidoFalse(usuarioLivroDTO.getUsuario_id());
            Optional<Livro> livro = livroRepository.findByIdAndExcluidoFalse(usuarioLivroDTO.getLivro_id());

            if (usuario.isEmpty() || livro.isEmpty()) {
                return ResponseEntity.ok("Usuário ou Livro não existente.");
            }

            List<UsuarioLivro> alugueis = usuarioLivroRepository.findByAtivoTrueAndUsuarioId(usuarioLivroDTO.getUsuario_id());

            if (alugueis.size() >= 2) {
                return ResponseEntity.ok("Máximo de dois livros por usuário. Devolva antes de pegar mais.");
            }

            if (isEmprestado(livro.get().getId().intValue())) {
                return ResponseEntity.ok("Livro já emprestado.");
            }

            UsuarioLivro registroEmprestimo = new UsuarioLivro();
            registroEmprestimo.setUsuario(usuario.get());
            registroEmprestimo.setLivro(livro.get());
            registroEmprestimo.setAtivo(true);

            usuarioLivroRepository.save(registroEmprestimo);
            return ResponseEntity.ok("Livro emprestado com sucesso.");
        }

        return ResponseEntity.ok("Livro já alugado pelo usuário.");
    }

    public ResponseEntity<String> devolver(UsuarioLivroDTO usuarioLivroDTO) {
        if (usuarioLivroDTO.getLivro_id() <= 0 || usuarioLivroDTO.getUsuario_id() <= 0) {
            return ResponseEntity.ok("É necessário o ID do livro e o ID do usuário. Zero é inválido.");
        }

        Optional<UsuarioLivro> registroExistente = usuarioLivroRepository.findActiveUsuarioLivrosByUsuarioIdAndLivroId(usuarioLivroDTO.getUsuario_id(), usuarioLivroDTO.getLivro_id());

        if (registroExistente.isEmpty()) {
            return ResponseEntity.ok("Empréstimo não existente.");
        }

        UsuarioLivro emprestimo = registroExistente.get();
        emprestimo.setAtivo(false);
        usuarioLivroRepository.save(emprestimo);

        return ResponseEntity.ok("Livro devolvido.");
    }


    private ResponseEntity<List<LivroPesquisaDTO>> isEmpty(List<LivroPesquisaDTO> livrosPesquisaDTO) {
        if (livrosPesquisaDTO.isEmpty()) {
            LivroPesquisaDTO livroVazio = new LivroPesquisaDTO();
            livroVazio.setId(0L);
            livroVazio.setAutor("Sem resultados.");
            livrosPesquisaDTO.add(livroVazio);
        }

        return new ResponseEntity<>(livrosPesquisaDTO, HttpStatus.OK);
    }

    private List<LivroPesquisaDTO> convertToDTO(List<Livro> livros) {
        List<LivroPesquisaDTO> livrosPesquisaDTO = new ArrayList<>();

        for (Livro livro : livros) {
            LivroPesquisaDTO livroPesquisaDTO = new LivroPesquisaDTO();

            livroPesquisaDTO.setId(livro.getId());
            livroPesquisaDTO.setAutor(livro.getAutor());
            livroPesquisaDTO.setTitulo(livro.getTitulo());
            livroPesquisaDTO.setEmprestado(isEmprestado(livro.getId().intValue()) ? "Sim" : "Não");

            livrosPesquisaDTO.add(livroPesquisaDTO);
        }

        return livrosPesquisaDTO;
    }

    private boolean isEmprestado(int id) {
        Optional<UsuarioLivro> livro = usuarioLivroRepository.findByAtivoTrueAndLivroId(id);
        return livro.isPresent();
    }
}

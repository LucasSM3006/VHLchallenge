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
        String mensagem;

        livroDTO.setAutor(livroDTO.getAutor() == null ? " " : livroDTO.getAutor());
        livroDTO.setTitulo(livroDTO.getTitulo() == null ? " " : livroDTO.getTitulo());

        if(!livroDTO.getTitulo().isBlank() && !livroDTO.getAutor().isBlank()) {
            Livro novoLivro = new Livro();

            novoLivro.setAutor(livroDTO.getAutor());
            novoLivro.setTitulo(livroDTO.getTitulo());
            novoLivro.setExcluido(false);

            livroRepository.save(novoLivro);
            mensagem = "Livro salvo.";
        } else {
            mensagem = "Informações faltando. Autor e Titulo são obrigatórios.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<List<LivroPesquisaDTO>> pesquisarPorAutor(LivroDTO livroDTO) {
        List<Livro> livros = livroRepository.findByAutorContainingIgnoreCaseAndExcluidoIsFalse(livroDTO.getAutor());
        List<LivroPesquisaDTO> livrosDTO = convertToDTO(livros);
        isEmpty(livrosDTO);

        return new ResponseEntity<>(livrosDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<LivroPesquisaDTO>> pesquisarPorTitulo(LivroDTO livroDTO) {
        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCaseAndExcluidoIsFalse(livroDTO.getTitulo());
        List<LivroPesquisaDTO> livrosDTO = convertToDTO(livros);
        isEmpty(livrosDTO);

        return new ResponseEntity<>(livrosDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<LivroPesquisaDTO>> pesquisarPorTituloEAutor(LivroDTO livroDTO) {
        List<Livro> livros = livroRepository.findByAutorContainingIgnoreCaseAndTituloContainingIgnoreCaseAndExcluidoIsFalse(livroDTO.getAutor(), livroDTO.getTitulo());
        List<LivroPesquisaDTO> livrosDTO = convertToDTO(livros);
        isEmpty(livrosDTO);

        return new ResponseEntity<>(livrosDTO, HttpStatus.OK);
    }

    public ResponseEntity<String> editar(int id, LivroDTO livroDTO) {
        String mensagem;

        Optional<Livro> livro = livroRepository.findByIdAndExcluidoFalse(id);

        livroDTO.setAutor(livroDTO.getAutor() == null ? " " : livroDTO.getAutor());
        livroDTO.setTitulo(livroDTO.getTitulo() == null ? " " : livroDTO.getTitulo());

        if(livroDTO.getAutor().isBlank() && livroDTO.getTitulo().isBlank()) {
            mensagem = "Informações faltando. É necessário ao menos o título ou autor.";
        } else if(livro.isPresent()) {
            Livro livroExistente = livro.get();

            String autorAnterior = livroExistente.getAutor();
            String tituloAnterior = livroExistente.getTitulo();

            livroExistente.setTitulo(livroDTO.getTitulo().isBlank() ? tituloAnterior : livroDTO.getTitulo());
            livroExistente.setAutor(livroDTO.getAutor().isBlank() ? autorAnterior : livroDTO.getAutor());

            if(autorAnterior.equals(livroExistente.getAutor()) && tituloAnterior.equals(livroExistente.getTitulo())) {
                mensagem = "Informações iguais, nada foi mudado.";
            } else {
                livroRepository.save(livroExistente);
                mensagem = "Livro editado.";
            }
        } else {
            mensagem = "Livro não existente.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<String> excluir(int id) {
        livroRepository.deleteById(id);
        return new ResponseEntity<>("Ok.", HttpStatus.OK);
    }

    public ResponseEntity<String> emprestar(UsuarioLivroDTO usuarioLivroDTO) {
        String mensagem;

        if(usuarioLivroDTO.getLivro_id() <= 0 || usuarioLivroDTO.getUsuario_id() <= 0) {
            mensagem = "É necessário o ID do livro e o ID do usuário. Zero é inválido.";
        } else {
            Optional<UsuarioLivro> registroExistente = usuarioLivroRepository.findActiveUsuarioLivrosByUsuarioIdAndLivroId(usuarioLivroDTO.getUsuario_id(), usuarioLivroDTO.getLivro_id());

            if(registroExistente.isEmpty()) {
                Optional<Usuario> usuario = usuarioRepository.findByIdAndExcluidoFalse(usuarioLivroDTO.getUsuario_id());
                Optional<Livro> livro = livroRepository.findByIdAndExcluidoFalse(usuarioLivroDTO.getLivro_id());

                if (usuario.isPresent() && livro.isPresent()) {
                    List<UsuarioLivro> alugueis = usuarioLivroRepository.findByAtivoTrueAndUsuarioId(usuarioLivroDTO.getUsuario_id());
                    if(alugueis.size() >= 2) {
                        mensagem = "Máximo de dois livros por usuário. Devolva antes de pegar mais.";
                    } else {
                        if(isEmprestado(livro.get().getId().intValue())) {
                            mensagem = "Livro já emprestado.";
                        } else {
                            UsuarioLivro registroEmprestimo = new UsuarioLivro();

                            registroEmprestimo.setUsuario(usuario.get());
                            registroEmprestimo.setLivro(livro.get());
                            registroEmprestimo.setAtivo(true);

                            usuarioLivroRepository.save(registroEmprestimo);
                            mensagem = "Livro emprestado com sucesso.";
                        }
                    }
                } else {
                    mensagem = "Usuário ou Livro não existente.";
                }
            } else {
                mensagem = "Livro já alugado pelo usuário.";
            }
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<String> devolver(UsuarioLivroDTO usuarioLivroDTO) {
        String mensagem;

        if(usuarioLivroDTO.getLivro_id() <= 0 || usuarioLivroDTO.getUsuario_id() <= 0) {
            mensagem = "É necessário o ID do livro e o ID do usuário. Zero é inválido.";
        } else {
            Optional<UsuarioLivro> registroExistente = usuarioLivroRepository.findActiveUsuarioLivrosByUsuarioIdAndLivroId(usuarioLivroDTO.getUsuario_id(), usuarioLivroDTO.getLivro_id());

            if(registroExistente.isEmpty()) {
                mensagem = "Empréstimo não existente.";
            } else {
                UsuarioLivro emprestimo = registroExistente.get();

                emprestimo.setAtivo(false);
                usuarioLivroRepository.save(emprestimo);

                mensagem = "Livro devolvido.";
            }
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    private List<LivroPesquisaDTO> isEmpty(List<LivroPesquisaDTO> livrosPesquisaDTO) {
        if(livrosPesquisaDTO.isEmpty()) {
            LivroPesquisaDTO livroVazio = new LivroPesquisaDTO();
            livroVazio.setId(0L);
            livroVazio.setAutor("Sem resultados.");
            livrosPesquisaDTO.add(livroVazio);
        }

        return livrosPesquisaDTO;
    }

    private List<LivroPesquisaDTO> convertToDTO(List<Livro> livros) {
        List<LivroPesquisaDTO> livrosPesquisaDTO = new ArrayList<>();

        if(livros.isEmpty()) return livrosPesquisaDTO;

        for(Livro livro : livros) {
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

        return (livro.isPresent());
    }
}

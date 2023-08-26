package com.vhl.library.services;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.model.DTO.LivroPesquisaDTO;
import com.vhl.library.model.Livro;
import com.vhl.library.repos.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
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
            novoLivro.setEmprestado(false);

            livroRepository.save(novoLivro);
            mensagem = "Livro salvo.";
        } else {
            mensagem = "Informações faltando. Autor e Titulo são obrigatórios.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
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
            livroPesquisaDTO.setEmprestado(livro.isEmprestado() ? "Sim" : "Não");

            livrosPesquisaDTO.add(livroPesquisaDTO);
        }

        return livrosPesquisaDTO;
    }
}

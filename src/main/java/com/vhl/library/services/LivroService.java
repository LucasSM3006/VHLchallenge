package com.vhl.library.services;

import com.vhl.library.model.DTO.LivroDTO;
import com.vhl.library.model.Livro;
import com.vhl.library.repos.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public ResponseEntity<String> adicionar(LivroDTO livroDTO) {
        String mensagem = "";

        if(!livroDTO.getTitulo().isBlank() && !livroDTO.getAutor().isBlank()) {
            Livro novoLivro = new Livro();

            novoLivro.setAutor(livroDTO.getAutor());
            novoLivro.setTitulo(livroDTO.getTitulo());

            livroRepository.save(novoLivro);
            mensagem = "Livro salvo.";
        } else {
            mensagem = "Informações faltando. Autor e Titulo são obrigatórios.";
        }

        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    public ResponseEntity<String> editar(int id, LivroDTO livroDTO) {
        return new ResponseEntity<>(livroDTO.getTitulo(), HttpStatus.OK);
    }

    public ResponseEntity<String> excluir(int id) {
        livroRepository.deleteById(id);
        return new ResponseEntity<>("Ok.", HttpStatus.OK);
    }

    public ResponseEntity<List<LivroDTO>> pesquisarPorAutor(LivroDTO livroDTO) {
        List<Livro> livros = livroRepository.findByAutorContainingIgnoreCase(livroDTO.getAutor());
        List<LivroDTO> livrosDTO = convertToDTO(livros);
        isEmpty(livrosDTO);

        return new ResponseEntity<>(livrosDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<LivroDTO>> pesquisarPorTitulo(LivroDTO livroDTO) {
        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(livroDTO.getTitulo());
        List<LivroDTO> livrosDTO = convertToDTO(livros);
        isEmpty(livrosDTO);

        return new ResponseEntity<>(livrosDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<LivroDTO>> pesquisarPorTituloEAutor(LivroDTO livroDTO) {
        List<Livro> livros = livroRepository.findByAutorContainingIgnoreCaseAndTituloContainingIgnoreCase(livroDTO.getAutor(), livroDTO.getTitulo());
        List<LivroDTO> livrosDTO = convertToDTO(livros);
        isEmpty(livrosDTO);

        return new ResponseEntity<>(livrosDTO, HttpStatus.OK);
    }

    private List<LivroDTO> isEmpty(List<LivroDTO> livrosDTO) {
        if(livrosDTO.isEmpty()) {
            LivroDTO livroVazio = new LivroDTO();
            livroVazio.setId(0L);
            livroVazio.setAutor("Sem resultados.");
            livrosDTO.add(livroVazio);
        }

        return livrosDTO;
    }

    private List<LivroDTO> convertToDTO(List<Livro> livros) {
        List<LivroDTO> livrosDTO = new ArrayList<>();

        if(livros.isEmpty()) return livrosDTO;

        for(Livro livro : livros) {
            LivroDTO livroDTO = new LivroDTO();
            livroDTO.setId(livro.getId());
            livroDTO.setAutor(livro.getAutor());
            livroDTO.setTitulo(livro.getTitulo());

            livrosDTO.add(livroDTO);
        }

        return livrosDTO;
    }
}

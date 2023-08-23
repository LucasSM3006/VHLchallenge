package com.vhl.library.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITULO")
    private String titulo;
    @Column(name = "AUTOR")
    private String autor;

    @OneToMany(mappedBy = "livro")
    private List<UsuarioLivro> usuarioLivro = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<UsuarioLivro> getUsuarioLivro() {
        return usuarioLivro;
    }

    public void setUsuarioLivro(List<UsuarioLivro> usuarioLivro) {
        this.usuarioLivro = usuarioLivro;
    }
}

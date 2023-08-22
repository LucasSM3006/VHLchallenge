package com.vhl.library.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livro")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;

    @OneToMany(mappedBy = "livro")
    private List<UsuarioLivro> usuarioLivro = new ArrayList<>();
}

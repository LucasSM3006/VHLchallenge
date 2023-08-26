package com.vhl.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_livro")
public class UsuarioLivro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "LIVRO_ID")
    private Livro livro;

    private boolean ativo;
}

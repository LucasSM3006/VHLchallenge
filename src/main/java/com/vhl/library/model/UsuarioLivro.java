package com.vhl.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_livro")
public class UsuarioLivro {
    @Id
    @ManyToOne
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "LIVRO_ID")
    private Livro livro;
}

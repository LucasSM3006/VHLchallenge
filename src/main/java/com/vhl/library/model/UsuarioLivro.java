package com.vhl.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_livro")
public class UsuarioLivro {
    @Id
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "livro_id")
    private Livro livro;
}

package com.vhl.library.services;

import com.vhl.library.repos.LivroRepository;
import org.springframework.stereotype.Service;

@Service
public class LivroService implements com.vhl.library.services.Service {
    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public String adicionar() {
        return null;
    }

    @Override
    public String editar() {
        return null;
    }

    @Override
    public String excluir() {
        return null;
    }
}

package com.vhl.library.services;

import com.vhl.library.repos.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements com.vhl.library.services.Service {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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

package com.hq.springcloud.msvc.usuarios.service;

import com.hq.springcloud.msvc.usuarios.models.entity.Usuario;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);

    Optional<Usuario> porEmail(String email);
}

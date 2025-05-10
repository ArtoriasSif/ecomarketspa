package com.nebula.msvc_usuarios.service;


import com.nebula.msvc_usuarios.model.Usuario;

public interface UsuarioService {

    Usuario save(Usuario usuario);
    Usuario findById(Long id);
    void deleteById(Long id);

}

package com.nebula.msvc_usuarios.service;


import com.nebula.msvc_usuarios.dto.UsuarioUpdateDTO;
import com.nebula.msvc_usuarios.model.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario save(Usuario usuario);
    Usuario findById(Long id);
    void deleteById(Long id);
    List<Usuario> findAll ();
    Usuario updateUsuario(Long Id, UsuarioUpdateDTO usuarioUpdateDTO);
}

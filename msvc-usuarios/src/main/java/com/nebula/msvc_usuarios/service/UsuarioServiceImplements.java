package com.nebula.msvc_usuarios.service;

import com.nebula.msvc_usuarios.dto.UsuarioUpdateDTO;
import com.nebula.msvc_usuarios.exception.UsuarioException;
import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImplements implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getIdUsuario() != null && usuarioRepository.findById(usuario.getIdUsuario()).isPresent()) {
            throw new UsuarioException("Usuario " +usuario.getNombreUsuario()+ " ya existente.");
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public void deleteById(Long Id) {
        if(usuarioRepository.findById(Id).isPresent()) {
            usuarioRepository.deleteById(Id);
        }else{
            throw new UsuarioException("No se encontro el usuario con id: " + Id);
        }
    }

    @Transactional
    @Override
    public Usuario updateUsuario(Long Id, UsuarioUpdateDTO usuarioUpdateDTO) {
        Usuario usuario = usuarioRepository.findById(Id).orElseThrow(
                () -> new UsuarioException("No se encontro el usuario con id: " + Id)
        );
        usuario.setContraUsuario(usuarioUpdateDTO.getContraUsuario());
        usuario.setNombreDelUsuario(usuarioUpdateDTO.getNombreDelUsuario());
        usuario.setCorreoUsuario(usuarioUpdateDTO.getCorreoUsuario());
        usuario.setDireccionUsuario(usuarioUpdateDTO.getDireccionUsuario());
        usuario.setTelefonoUsuario(usuarioUpdateDTO.getTelefonoUsuario());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public Usuario findById(Long Id) {
        return usuarioRepository.findById(Id).orElseThrow(
                () -> new UsuarioException("Usuario no encontrado.") );
    }

    @Transactional
    @Override
    public List<Usuario> findAll () {
        return usuarioRepository.findAll() ;
    }
}

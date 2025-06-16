package com.nebula.msvc_usuarios.service;

import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplementsTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImplements usuarioServiceImplements;
    private Usuario usuarioPrueba;


    @Test
    void save() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateUsuario() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }
}
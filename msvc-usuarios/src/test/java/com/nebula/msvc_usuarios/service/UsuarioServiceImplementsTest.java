package com.nebula.msvc_usuarios.service;

import com.nebula.msvc_usuarios.dto.UsuarioUpdateDTO;
import com.nebula.msvc_usuarios.exception.UsuarioException;
import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplementsTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImplements usuarioServiceImplements;

    private Usuario usuarioPrueba;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
        usuarioPrueba = new Usuario();
        usuarioPrueba.setIdUsuario(1L);
        usuarioPrueba.setNombreUsuario(faker.name().username());
        usuarioPrueba.setCorreoUsuario(faker.internet().emailAddress());
        usuarioPrueba.setContraUsuario("password123");
        usuarioPrueba.setDireccionUsuario(faker.address().streetAddress());
        usuarioPrueba.setTelefonoUsuario(faker.phoneNumber().phoneNumber());
    }



    @Test
    @DisplayName("Debe lanzar excepción al guardar un usuario ya existente")
    public void debeLanzarExcepcionUsuarioExistente() {
        when(usuarioRepository.findById(usuarioPrueba.getIdUsuario())).thenReturn(Optional.of(usuarioPrueba));
        assertThatThrownBy(() -> usuarioServiceImplements.save(usuarioPrueba))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("ya existente");

        verify(usuarioRepository, times(1)).findById(usuarioPrueba.getIdUsuario());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un usuario existente por ID")
    public void debeEliminarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioPrueba));
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioServiceImplements.deleteById(1L);

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra usuario al eliminar")
    public void debeLanzarExcepcionAlEliminarUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioServiceImplements.deleteById(99L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("No se encontro el usuario");

        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debe actualizar los datos de un usuario existente")
    public void debeActualizarUsuario() {
        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO();
        updateDTO.setNombreDelUsuario("Nuevo Nombre");
        updateDTO.setCorreoUsuario("nuevo@email.com");
        updateDTO.setContraUsuario("nuevaPassword");
        updateDTO.setDireccionUsuario("Nueva Dirección");
        updateDTO.setTelefonoUsuario("999999999");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioPrueba));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado = usuarioServiceImplements.updateUsuario(1L, updateDTO);

        assertThat(resultado.getNombreDelUsuario()).isEqualTo("Nuevo Nombre");
        assertThat(resultado.getCorreoUsuario()).isEqualTo("nuevo@email.com");

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuarioPrueba);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar usuario inexistente")
    public void debeLanzarExcepcionAlActualizarUsuarioInexistente() {
        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO();
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioServiceImplements.updateUsuario(2L, updateDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("No se encontro el usuario");

        verify(usuarioRepository, times(1)).findById(2L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe encontrar un usuario por ID")
    public void debeEncontrarUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioPrueba));

        Usuario resultado = usuarioServiceImplements.findById(1L);

        assertThat(resultado).isEqualTo(usuarioPrueba);

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción si no encuentra usuario por ID")
    public void debeLanzarExcepcionAlBuscarUsuarioPorIdInexistente() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioServiceImplements.findById(999L))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(usuarioRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe guardar un nuevo usuario")
    public void debeGuardarUsuarioNuevo() {
        when(usuarioRepository.findById(usuarioPrueba.getIdUsuario())).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuarioPrueba)).thenReturn(usuarioPrueba);

        Usuario resultado = usuarioServiceImplements.save(usuarioPrueba);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreUsuario()).isEqualTo(usuarioPrueba.getNombreUsuario());

        verify(usuarioRepository, times(1)).findById(usuarioPrueba.getIdUsuario());
        verify(usuarioRepository, times(1)).save(usuarioPrueba);
    }

    @Test
    @DisplayName("Debe listar todos los usuarios")
    public void debeListarTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuarioPrueba);
        usuarios.add(new Usuario("otroUser", "otro@email.com", "21392432-k", "123 Calle falsa", "+56 912345678"));

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioServiceImplements.findAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getIdUsuario()).isEqualTo(1L);

        verify(usuarioRepository, times(1)).findAll();
    }
}

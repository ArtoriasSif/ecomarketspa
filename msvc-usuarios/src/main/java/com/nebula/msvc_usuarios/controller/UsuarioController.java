package com.nebula.msvc_usuarios.controller;

import com.nebula.msvc_usuarios.dto.UsuarioUpdateDTO;
import com.nebula.msvc_usuarios.model.Usuario;
import com.nebula.msvc_usuarios.service.UsuarioService;
import com.nebula.msvc_usuarios.service.UsuarioServiceImplements;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/v1/usuarios")
@Validated
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usuarioService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll());
    }

    @PostMapping()
    public ResponseEntity<Usuario> createUsuario(@Validated @RequestBody Usuario usuario) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioService.save(usuario));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @Validated @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.updateUsuario(id, usuarioUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id) {
        try{
            usuarioService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Usuario eliminado exitosamente");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}

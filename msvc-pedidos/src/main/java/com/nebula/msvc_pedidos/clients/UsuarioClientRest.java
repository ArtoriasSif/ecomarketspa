package com.nebula.msvc_pedidos.clients;


import com.nebula.msvc_pedidos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "http://localhost:8084/api/usuarios")
public interface UsuarioClientRest {

    @GetMapping
    List<Usuario> findAll();

    @GetMapping("/{id}")
    Usuario findByIdUsuario(@PathVariable("id") Long id);
}

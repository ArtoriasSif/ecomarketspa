package com.nebula.msvc_detalle_pedido.clients;

import com.nebula.msvc_detalle_pedido.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios", url = "http://localhost:8084/api/v1/usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario findByIdUsuario(@PathVariable("id") Long id);
}

package han.msvc_feedback.mscv_feedback.client;

import han.msvc_feedback.mscv_feedback.model.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios",url = "http://localhost:8084/api/v1/usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
        ResponseEntity<Usuario> findByIdUsuario(@PathVariable Long id);
}

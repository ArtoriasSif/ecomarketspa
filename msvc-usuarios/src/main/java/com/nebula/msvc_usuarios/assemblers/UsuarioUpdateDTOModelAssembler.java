package com.nebula.msvc_usuarios.assemblers;

import com.nebula.msvc_usuarios.controller.UsuarioControllerV2;
import com.nebula.msvc_usuarios.dto.UsuarioUpdateDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UsuarioUpdateDTOModelAssembler implements RepresentationModelAssembler<UsuarioUpdateDTO, EntityModel<UsuarioUpdateDTO>> {
    @Override
    public EntityModel<UsuarioUpdateDTO> toModel(UsuarioUpdateDTO entity) {
        Long userId = entity.getIdUsuario();

        return EntityModel.of(
                entity,
                linkTo(methodOn(UsuarioControllerV2.class).findById(userId)).withSelfRel(),
                linkTo(methodOn(UsuarioControllerV2.class).findAll()).withRel("usuarios")
        );
}
}

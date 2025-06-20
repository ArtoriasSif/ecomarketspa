package com.nebulosa.msvc_inventario.assemblers;

import com.nebulosa.msvc_inventario.controllers.InventoryControllerV2;
import com.nebulosa.msvc_inventario.dtos.InventoryResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventoryResponseDTOAssembler implements RepresentationModelAssembler<InventoryResponseDTO, EntityModel<InventoryResponseDTO>> {

    @Override
    public EntityModel<InventoryResponseDTO> toModel(InventoryResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(InventoryControllerV2.class).findById(dto.getIdInventario())).withSelfRel(),
                linkTo(methodOn(InventoryControllerV2.class).findAllWithDetails()).withRel("inventarios-detallados")
        );
    }
}

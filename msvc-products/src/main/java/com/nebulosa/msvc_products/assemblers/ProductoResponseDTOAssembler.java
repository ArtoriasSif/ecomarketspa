package com.nebulosa.msvc_products.assemblers;

import com.nebulosa.msvc_products.controllers.ProductControllerV2;
import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoResponseDTOAssembler implements RepresentationModelAssembler<ProductoResponseDTO, EntityModel<ProductoResponseDTO>> {

    @Override
    public EntityModel<ProductoResponseDTO> toModel(ProductoResponseDTO dto) {
        return EntityModel.of(
                dto,
                linkTo(methodOn(ProductControllerV2.class).getProductosByNombre(dto.getNombreProducto())).withSelfRel(),
                linkTo(methodOn(ProductControllerV2.class).getAllProducts()).withRel("productos")
        );
    }
}

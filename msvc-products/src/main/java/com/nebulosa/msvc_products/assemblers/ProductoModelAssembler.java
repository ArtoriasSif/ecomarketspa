package com.nebulosa.msvc_products.assemblers;

import com.nebulosa.msvc_products.controllers.ProductControllerV2;
import com.nebulosa.msvc_products.models.entities.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {
    @Override
    public EntityModel<Product> toModel(Product entity) {
        return EntityModel.of(
                entity,
                linkTo(methodOn(ProductControllerV2.class).findByIdProducto(entity.getIdProducto())).withSelfRel(),
                linkTo(methodOn(ProductControllerV2.class).getAllProducts()).withRel("productos")
        );
    }
}

package com.nebulosa.msvc_inventario.assemblers;

import com.nebulosa.msvc_inventario.controllers.InventoryControllerV2;
import com.nebulosa.msvc_inventario.models.entities.Inventory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventoryEntityModelAssembler implements RepresentationModelAssembler<Inventory, EntityModel<Inventory>> {

    @Override
    public EntityModel<Inventory> toModel(Inventory inventory) {
        return EntityModel.of(inventory,
                linkTo(methodOn(InventoryControllerV2.class).findById(inventory.getIdInventario())).withSelfRel(),
                linkTo(methodOn(InventoryControllerV2.class).findAll()).withRel("inventarios"),
                linkTo(methodOn(InventoryControllerV2.class).findByIdSucursal(inventory.getIdSucursal())).withRel("inventarios-por-sucursal")

        );
    }
}
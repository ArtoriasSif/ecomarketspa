package com.cmunoz.msvc.sucursal.assemblers;


import com.cmunoz.msvc.sucursal.controller.SucursalControllerV2;
import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SucursalModelAssembler implements RepresentationModelAssembler<Sucursal, EntityModel<Sucursal>> {

    @Override
    public EntityModel<Sucursal> toModel(Sucursal entity) {
        //Link link = Link.of("http://localhost:8089/api/v1/Sucursal/"+entity.getIdSucursal()).withRel("sucursal");
        return EntityModel.of(
                entity,
                linkTo(methodOn(SucursalControllerV2.class).getSucursalFindById(entity.getIdSucursal())).withSelfRel(),
                linkTo(methodOn(SucursalControllerV2.class).getAllSucursales()).withRel("sucursales")
                //Link.of("http://localhost:8089/api/v1/Sucursal/"+entity.getIdSucursal()).withRel("sucursal")
        );
    }

}

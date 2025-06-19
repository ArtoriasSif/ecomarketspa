package com.nebula.msvc_pedidos.assemblers;

import com.nebula.msvc_pedidos.controllers.PedidoControllerV2;
import com.nebula.msvc_pedidos.dtos.PedidoConDetalleDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoConDetalleModelAssembler implements RepresentationModelAssembler<PedidoConDetalleDTO, EntityModel<PedidoConDetalleDTO>> {

    @Override
    public EntityModel<PedidoConDetalleDTO> toModel(PedidoConDetalleDTO dto) {
        return EntityModel.of(
                dto,
                linkTo(methodOn(PedidoControllerV2.class).findByIdPedido(dto.getIdPedido())).withSelfRel(),
                linkTo(methodOn(PedidoControllerV2.class).findAll()).withRel("todos-los-pedidos"),
                linkTo(methodOn(PedidoControllerV2.class).findAllPedidosConDetalle()).withSelfRel()
        );
    }
}


package com.nebula.msvc_pedidos.assemblers;

import com.nebula.msvc_pedidos.controllers.PedidoControllerV2;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(
                pedido,
                linkTo(methodOn(PedidoControllerV2.class).findById(pedido.getIdPedido())).withRel("detalle-pedido")
        );
    }
}


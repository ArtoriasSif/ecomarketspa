package com.nebula.msvc_detalle_pedido.assemblers;

import com.nebula.msvc_detalle_pedido.dtos.UpdateCuantidadProductoDetallePedidoResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateCantidadDetallePedidoModelAssembler implements RepresentationModelAssembler<UpdateCuantidadProductoDetallePedidoResponseDTO, EntityModel<UpdateCuantidadProductoDetallePedidoResponseDTO>> {

    private static final String PEDIDO_SERVICE_BASE_URL = "http://localhost:8085/api/v2/pedido";
    private static final String PRODUCTO_SERVICE_BASE_URL = "http://localhost:8082/api/v1/producto";

    @Override
    public EntityModel<UpdateCuantidadProductoDetallePedidoResponseDTO> toModel(UpdateCuantidadProductoDetallePedidoResponseDTO dto) {
        List<Link> links = new ArrayList<>();

        links.add(Link.of(PEDIDO_SERVICE_BASE_URL + "/" + dto.getIdPedido()).withRel("pedido"));
        // Agrega link producto solo si tienes idProducto (añádelo al DTO si es necesario)
        // links.add(Link.of(PRODUCTO_SERVICE_BASE_URL + "/" + dto.getIdProducto()).withRel("producto"));

        return EntityModel.of(dto, links);
    }
}

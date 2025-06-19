package com.nebula.msvc_detalle_pedido.assemblers;


import com.nebula.msvc_detalle_pedido.controllers.DetallePedidoControllerV2;
import com.nebula.msvc_detalle_pedido.dtos.DetallePedidoResponseDTO;
import com.nebula.msvc_detalle_pedido.models.entities.DetallePedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;



@Component
public class DetallePedidoModelAssembler implements RepresentationModelAssembler<DetallePedidoResponseDTO, EntityModel<DetallePedidoResponseDTO>> {

    private static final String PEDIDO_SERVICE_BASE_URL = "http://localhost:8085/api/v2/pedido";
    private static final String PRODUCTO_SERVICE_BASE_URL = "http://localhost:8082/api/v1/producto";

    @Override
    public EntityModel<DetallePedidoResponseDTO> toModel(DetallePedidoResponseDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(DetallePedidoControllerV2.class).findDetallesByIdPedido(dto.getIdPedido())).withRel("pedido-detalles"),
                Link.of(PEDIDO_SERVICE_BASE_URL + "/" + dto.getIdPedido()).withRel("pedido"),
                Link.of(PRODUCTO_SERVICE_BASE_URL + "/" + dto.getIdProducto()).withRel("producto")
        );
    }
}

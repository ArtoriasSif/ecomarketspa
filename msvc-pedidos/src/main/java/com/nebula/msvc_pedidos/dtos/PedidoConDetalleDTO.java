package com.nebula.msvc_pedidos.dtos;

import com.nebula.msvc_pedidos.models.DetallePedido;
import com.nebula.msvc_pedidos.models.entitis.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PedidoConDetalleDTO {
    private String nombreUsuario;
    private String rutUsuario;
    private String nombreSucursal;
    private List<DetallePedido> detalles;
    private Double total;
}

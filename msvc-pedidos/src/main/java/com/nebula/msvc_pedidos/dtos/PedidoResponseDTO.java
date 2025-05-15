package com.nebula.msvc_pedidos.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private String nombreUsuario;
    private String nombreSucursal;
    private List<ItemDetalleDTO> detalles;
    private double total;
}

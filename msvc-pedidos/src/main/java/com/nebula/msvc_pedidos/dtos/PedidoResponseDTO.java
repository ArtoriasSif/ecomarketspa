package com.nebula.msvc_pedidos.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PedidoResponseDTO {

    private String nombreUsuario;
    private String emailUsuario;
    private String nombreSucursal;
    private String direccionSucursal;
    private String provinciaSucursal;

    private LocalDateTime fecha;
    private Double totalPedido;

    private List<ItemPedidoResponseDTO> productos;
}

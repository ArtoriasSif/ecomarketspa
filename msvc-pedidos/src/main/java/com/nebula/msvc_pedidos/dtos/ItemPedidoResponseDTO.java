package com.nebula.msvc_pedidos.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemPedidoResponseDTO {
    private String nombre;
    private Double precioUnitario;
    private Long cantidad;
    private Double subtotal;
}

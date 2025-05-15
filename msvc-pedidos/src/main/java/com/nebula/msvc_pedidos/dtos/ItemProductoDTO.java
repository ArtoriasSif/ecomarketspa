package com.nebula.msvc_pedidos.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemProductoDTO {
    private Long idProducto;
    private Long cantidad;
}

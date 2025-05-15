package com.nebula.msvc_pedidos.dtos;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PedidoDTO {
    private Long idUsuario;
    private Long idSucursal;
    private List<ItemProductoDTO> productos;
}

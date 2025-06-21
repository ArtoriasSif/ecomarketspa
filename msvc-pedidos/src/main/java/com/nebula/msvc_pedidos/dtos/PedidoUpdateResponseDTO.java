package com.nebula.msvc_pedidos.dtos;

import lombok.*;

import java.time.LocalDateTime;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class PedidoUpdateResponseDTO {
    private String nombreUsuario;
    private String nombreSucursal;
    private LocalDateTime fechaActual;
    private String mensaje;

    public PedidoUpdateResponseDTO(String mensaje){
        this.mensaje = "Pedido actualizado con exito" ;
    }
}

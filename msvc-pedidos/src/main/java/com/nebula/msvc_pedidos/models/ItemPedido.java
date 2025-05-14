package com.nebula.msvc_pedidos.models;

import jakarta.persistence.*;

@Entity
@Table(name = "items_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemPedido;

    private Long productoId;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        this.subtotal = cantidad * precioUnitario;
    }

}
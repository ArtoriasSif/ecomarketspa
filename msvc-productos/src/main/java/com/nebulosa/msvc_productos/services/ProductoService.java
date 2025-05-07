package com.nebulosa.msvc_productos.services;

import com.nebulosa.msvc_productos.models.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> findAllProducto();
    Producto findByIdProducto(Long id);
    Producto save (Producto producto);

}

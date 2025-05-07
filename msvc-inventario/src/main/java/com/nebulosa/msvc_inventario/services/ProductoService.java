package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.models.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> findAllProducto();
    Producto findByIdProducto(Long id);
    Producto save (Producto producto);

 }

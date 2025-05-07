package com.nebulosa.msvc_producto.services;

import com.nebulosa.msvc_producto.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProducto();
    Product findByIdProducto(Long id);
    Product save (Product producto);
}

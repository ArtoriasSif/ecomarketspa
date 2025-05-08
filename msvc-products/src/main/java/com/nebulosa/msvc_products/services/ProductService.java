package com.nebulosa.msvc_products.services;

import com.nebulosa.msvc_products.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProduct();
    Product findByIdProducto(Long id);
    Product save (Product producto);
    Product updatePrice(Long id, Long price);
    void deleteByIdProducto(Long id);
}

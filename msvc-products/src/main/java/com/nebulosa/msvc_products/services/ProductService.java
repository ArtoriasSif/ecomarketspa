package com.nebulosa.msvc_products.services;

import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import com.nebulosa.msvc_products.models.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    List<ProductoResponseDTO> findAllProductDTO();
    Product findByIdProducto(Long id);
    Product findByNombreProducto(String nombreProducto);
    ProductoResponseDTO save(Product product);
    ProductoResponseDTO updatePrice(Long id, Double price);
    String deleteByIdProducto(Long id);
}

package com.nebulosa.msvc_producto.services;

import com.nebulosa.msvc_producto.exceptions.ProductException;
import com.nebulosa.msvc_producto.models.Product;
import com.nebulosa.msvc_producto.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAllProducto(){
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Product findByIdProducto(Long id){
        return productRepository.findById(id).orElseThrow(
                ()-> new ProductException("Producto con el id "+id+" no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public  Product save(Product producto){
        if (productRepository.findById(producto.getProductoId()).isPresent()){
            throw new ProductException("Producto con el id "+producto.getProductoId()+" ya existente");
        }

        return productRepository.save(producto);
    }
}

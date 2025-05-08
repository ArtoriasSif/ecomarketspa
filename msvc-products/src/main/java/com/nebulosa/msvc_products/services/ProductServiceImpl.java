package com.nebulosa.msvc_products.services;

import com.nebulosa.msvc_products.exceptions.ProductException;
import com.nebulosa.msvc_products.models.Product;
import com.nebulosa.msvc_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product>  findAllProduct(){
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
    public  Product save(Product product){
        if (productRepository.findByNombreProducto(product.getNombreProducto()).isPresent()){
            throw new ProductException("El producto con el nombre "+product.getNombreProducto()+" ya existe");
        }
        return productRepository.save(product);
    }

}

package com.nebulosa.msvc_products.services;

import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import com.nebulosa.msvc_products.exceptions.ProductException;
import com.nebulosa.msvc_products.models.Product;
import com.nebulosa.msvc_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoResponseDTO> findAllProductDTO() {
        List<Product> productos = productRepository.findAll();

        return productos.stream()
                .map(product -> new ProductoResponseDTO(
                        "Producto agregado exitosamente",
                        product.getPrecio(),
                        product.getNombreProducto()
                ))
                .collect(Collectors.toList());
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
    public Product findByNombreProducto(String nombre){
        return productRepository.findByNombreProducto(nombre).orElseThrow(
                ()-> new ProductException("Producto con el nombre "+nombre+" no encontrado")
        );
    }


    @Transactional(readOnly = true)
    @Override
    public ProductoResponseDTO save(Product product){
        String nombre = product.getNombreProducto().trim();
        String capitalizado = Arrays.stream(nombre.split("\\s+"))
                .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        //validar que no exista producto con ese nombre
        if (productRepository.findByNombreProducto(capitalizado).isPresent()){
            throw new ProductException("Ya existe un producto con el nombre: " + capitalizado);
        }
        product.setNombreProducto(capitalizado);
        productRepository.save(product);

        return new ProductoResponseDTO(
                "Producto del codigo : "+ product.getIdProducto()+" agregado exitosamente",
                product.getPrecio(),
                product.getNombreProducto()
        );
    }

    @Transactional
    @Override
    public Product updatePrice(Long id, Double price){
        return productRepository.findById(id).map(p -> {
            if (price < 0.0){
                throw new ProductException("El precio no puede ser negativo");
            }
            p.setPrecio(price);
            return productRepository.save(p);
        }).orElseThrow(
                () -> new ProductException("No se encontro el producto con id: " + id)
        );
    }

    @Transactional
    @Override
    public void deleteByIdProducto(Long id){
        if (productRepository.findById(id).isPresent()){
            productRepository.deleteById(id);
        }else{
            throw new ProductException("No se encontro el producto con id: " + id);
        }
    }
}

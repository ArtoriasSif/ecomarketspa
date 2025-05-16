package com.nebulosa.msvc_products.services;

import com.nebulosa.msvc_products.clients.InventarioClientRest;
import com.nebulosa.msvc_products.dtos.ProductoResponseDTO;
import com.nebulosa.msvc_products.exceptions.ProductException;
import com.nebulosa.msvc_products.models.Inventario;
import com.nebulosa.msvc_products.models.entities.Product;
import com.nebulosa.msvc_products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventarioClientRest inventarioClientRest;

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
    public ProductoResponseDTO updatePrice(Long id, Double price) {
        return productRepository.findById(id).map(p -> {
            if (price < 0.0) {
                throw new ProductException("El precio no puede ser negativo");
            }
            p.setPrecio(price);
            productRepository.save(p);
            return new ProductoResponseDTO(
                    "Precio del producto con id: " + p.getIdProducto() + " actualizado exitosamente",
                    price,
                    p.getNombreProducto()
            );
        }).orElseThrow(
                () -> new ProductException("No se encontro el producto con id: " + id)
        );

    }

    @Transactional
    @Override
    //Deletar el producto validando que no exista ningun inventario con ese producto
    public String deleteByIdProducto(Long id) {
        return productRepository.findById(id).map(producto -> {

            List<Inventario> inventarios = inventarioClientRest.findAll();

            Optional<Inventario> inventarioConStock = inventarios.stream()
                    .filter(i -> i.getIdProducto().equals(id) && i.getCantidad() > 0)
                    .findFirst();

            if (inventarioConStock.isPresent()) {
                Inventario i = inventarioConStock.get();
                throw new ProductException("No se puede eliminar el producto con id: " + id +
                        " porque tiene el inventario con id: " + i.getIdInventario() +
                        " con cantidad: " + i.getCantidad());
            }

            productRepository.deleteById(id);
            return "Producto " + producto.getNombreProducto() + " eliminado exitosamente";

        }).orElseThrow(() -> new ProductException("No se encontró el producto con id: " + id));
    }
}




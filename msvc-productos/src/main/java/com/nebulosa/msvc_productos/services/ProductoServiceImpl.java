package com.nebulosa.msvc_productos.services;

import com.nebulosa.msvc_productos.exceptions.ProductoException;
import com.nebulosa.msvc_productos.models.Producto;
import com.nebulosa.msvc_productos.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl {

    @Autowired
    private ProductoRepository inventarioRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAllProducto(){
        return inventarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Producto findByIdProducto(Long id){
        return inventarioRepository.findById(id).orElseThrow(
                ()-> new ProductoException("Producto con el id "+id+" no encontrado")
        );
    }

    @Transactional(readOnly = true)
    @Override
    public  Producto save(Producto producto){
        if (inventarioRepository.findById(producto.getProductoId()).isPresent()){
            throw new ProductoException("Producto con el id "+producto.getProductoId()+" ya existente");
        }

        return inventarioRepository.save(producto);
    }

}

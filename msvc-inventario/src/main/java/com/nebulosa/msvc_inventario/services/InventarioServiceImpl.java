package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.models.Inventario;
import com.nebulosa.msvc_inventario.repositories.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioServiceImpl implements InventarioService{

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    ProductoRepository productoRepository;

    public Inventario crearInventario(Long productoId, Long cantidad){
        Producto producto = productoRepository.findById(productoId).orElseThrow(
                ()-> new ProductoException("Producto no encontrado")
        );


    }


}

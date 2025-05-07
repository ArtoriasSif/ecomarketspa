package com.nebulosa.msvc_inventario.services;

import com.nebulosa.msvc_inventario.repositories.InventarioRepository;
import com.nebulosa.msvc_inventario.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioServiceImpl implements InventarioService{

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    ProductoRepository productoRepository;


}

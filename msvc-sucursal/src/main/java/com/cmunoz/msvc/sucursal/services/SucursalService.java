package com.cmunoz.msvc.sucursal.services;

import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;

import java.util.List;

public interface SucursalService {

    List<Sucursal> findAllSucursal();
    Sucursal findByNombreSucursal(String nombreSucursal);
    Sucursal findByIdSucursal(Long id);
    String save(Sucursal sucursal);
    String deleteByIdSucursal(Long idSucursal);
    String updateByIdSucursal(Long id, Sucursal sucursal);

}
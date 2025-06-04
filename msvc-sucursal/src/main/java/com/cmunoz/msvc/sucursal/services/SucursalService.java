package com.cmunoz.msvc.sucursal.services;

import com.cmunoz.msvc.sucursal.models.Sucursal;

import java.util.List;

public interface SucursalService {

    List<Sucursal> findAllSucursal();
    Sucursal findByNombreSucursal(String nombreSucursal);
    Sucursal findByIdSucursal(Long id);
    String save(Sucursal sucursal);
    void deleteByIdSucursal(Long id);
    String updateByIdSucursal(Long id, Sucursal sucursal);

}
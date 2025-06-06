package com.cmunoz.msvc.sucursal.services;


import com.cmunoz.msvc.sucursal.client.SucursalClientRest;
import com.cmunoz.msvc.sucursal.client.PedidoClientRest;
import com.cmunoz.msvc.sucursal.exception.SucursalException;
import com.cmunoz.msvc.sucursal.models.Entitys.Inventario;
import com.cmunoz.msvc.sucursal.models.Sucursal;
import com.cmunoz.msvc.sucursal.repositories.SucursalRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SucursalServicesImpl implements SucursalService {

    @Autowired
    private SucursalRepository SucursalRepository;

    @Autowired
    private SucursalClientRest sucursalClientRest;

    @Autowired
    private PedidoClientRest pedidoClientRest;

    @Override
    public List<Sucursal> findAllSucursal() {
        return SucursalRepository.findAll();
    }

    @Transactional
    @Override
    public Sucursal findByIdSucursal(Long id) {
        return SucursalRepository.findById(id)
                .orElseThrow(() -> new SucursalException("No se encontro la sucursal con id: " + id ));
    }


    @Transactional
    @Override
    public Sucursal findByNombreSucursal(String nombreSucursal) {
        return SucursalRepository.findByNombreSucursal(nombreSucursal).orElseThrow(
                ()-> new SucursalException("No se encontro la sucursal con nombre: " + nombreSucursal)
        );
    }

    @Transactional
    @Override
    public String save(Sucursal sucursal) {
        if (SucursalRepository.findByNombreSucursal(sucursal.getNombreSucursal()).isPresent()){

            throw new SucursalException("La sucursal con el nombre "+sucursal.getNombreSucursal()+" ya existe");
        }
        SucursalRepository.save(sucursal);
        return "La sucursal "+sucursal.getNombreSucursal()+" se agrego exitosamente";
    }

    @Transactional
    @Override
    public String deleteByIdSucursal(Long idSucursal) {
        Sucursal sucursal = SucursalRepository.findById(idSucursal).orElseThrow(
                () -> new SucursalException("No se encontro la sucursal con id: " + idSucursal)
        );
        // Eliminar inventarions de la sucursal
        List<Inventario> invetarioEliminar = sucursalClientRest.findByIdSucursal(idSucursal);

        for (Inventario inventario : invetarioEliminar) {
            sucursalClientRest.updateInventory(inventario.getIdInventario());
            sucursalClientRest.deleteInventoryById(inventario.getIdInventario());
        }
        // Eliminar todos los pedidos asociados a la sucursal
        pedidoClientRest.deletePedidoSucursal(idSucursal);

        SucursalRepository.deleteById(sucursal.getIdSucursal());

        return "Sucursal eliminada con éxito";
    }


    @Transactional
    @Override
    public String updateByIdSucursal(Long id, Sucursal sucursal) {
        if(!SucursalRepository.findById(id).isPresent()){
            throw new SucursalException("No se encontro la sucursal con id: " + id);
        }
        Sucursal sucursalUpdate = SucursalRepository.findById(id).get();
        sucursalUpdate.setNombreSucursal(sucursal.getNombreSucursal());
        sucursalUpdate.setDireccionSucursal(sucursal.getDireccionSucursal());
        sucursalUpdate.setTelefonoSucursal(sucursal.getTelefonoSucursal());
        sucursalUpdate.setCiudadSucursal(sucursal.getCiudadSucursal());
        sucursalUpdate.setProvinciaSucursal(sucursal.getProvinciaSucursal());
        sucursalUpdate.setRegionSucursal(sucursal.getRegionSucursal());
        sucursalUpdate.setEmailSucursal(sucursal.getEmailSucursal());
        SucursalRepository.save(sucursalUpdate);
        return "La sucursal con id: " + id + " se actualizo exitosamente";
    }
}

package com.cmunoz.msvc.sucursal.controller;


import com.cmunoz.msvc.sucursal.models.Sucursal;
import com.cmunoz.msvc.sucursal.services.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/sucursal")
@Validated
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<Sucursal>> getAllSucursales() {
        return ResponseEntity
                .ok()
                .body(sucursalService.findAllSucursal());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> getSucursalById(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(sucursalService.findById(id));
    }

    @GetMapping("/{nombreSucursal}")
    public ResponseEntity<Sucursal> getSucursalByNombreSucursal( @PathVariable String nombreSucursal) {
        return ResponseEntity
                .ok()
                .body(sucursalService.findByNombreSucursal(nombreSucursal));
    }

    @PostMapping()
    public ResponseEntity<Sucursal> saveSucursal(@Validated @RequestBody Sucursal sucursal) {
        return ResponseEntity
                .ok()
                .body(sucursalService.save(sucursal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSucursal(@PathVariable Long id) {
        try{
            sucursalService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Sucursal eliminada exitosamente");
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}

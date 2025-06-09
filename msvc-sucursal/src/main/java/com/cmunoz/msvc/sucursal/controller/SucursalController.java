package com.cmunoz.msvc.sucursal.controller;


import com.cmunoz.msvc.sucursal.models.Entitys.Sucursal;
import com.cmunoz.msvc.sucursal.services.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/v1/sucursal")
@Validated
public class SucursalController {

    //Falta agregar PUT
    @Autowired
    private SucursalService sucursalService;



    @GetMapping
    public ResponseEntity<List<Sucursal>> getAllSucursales() {
        return ResponseEntity
                .ok()
                .body(sucursalService.findAllSucursal());
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<Sucursal> getSucursalFindById(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.findByIdSucursal(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Sucursal> getSucursalByNombreSucursal( @PathVariable String nombre) {
        return ResponseEntity
                .status(200)
                .body(sucursalService.findByNombreSucursal(nombre));
    }

    @PostMapping()
    public ResponseEntity<String> saveSucursal(@Validated @RequestBody Sucursal sucursal) {
        return ResponseEntity
                .status(201)
                .body(sucursalService.save(sucursal));
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateSucursal(@PathVariable Long id, @Validated @RequestBody Sucursal sucursal) {
        try{
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalService.updateByIdSucursal(id, sucursal));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSucursal(@PathVariable Long id) {
        try{

            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalService.deleteByIdSucursal(id));

        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
    }
}

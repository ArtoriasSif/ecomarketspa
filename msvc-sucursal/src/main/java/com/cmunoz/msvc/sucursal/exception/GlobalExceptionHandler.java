package com.cmunoz.msvc.sucursal.exception;


import com.cmunoz.msvc.sucursal.dto.SucursalDTO;
import feign.RetryableException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private SucursalDTO createErrorDTO(int status, Date date , Map<String,String> errorMap){
        SucursalDTO errorDTO = new SucursalDTO();

        errorDTO.setStatus(status);
        errorDTO.setDate(date);
        errorDTO.setErrors(errorMap);

        return errorDTO;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SucursalDTO> handleValidationField(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        for (FieldError fieldErrorDTO : ex.getBindingResult().getFieldErrors()){
            errorMap.put(fieldErrorDTO.getField(),fieldErrorDTO.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createErrorDTO(HttpStatus.BAD_REQUEST.value(),new Date(),errorMap));
    }

    @ExceptionHandler(SucursalException.class)
    public ResponseEntity<SucursalDTO> handleSucursalException(SucursalException ex){
        Map<String,String> errorMap = Collections.singletonMap("sucursal",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(this.createErrorDTO(HttpStatus.NOT_FOUND.value(),new Date(),errorMap));

    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<Map<String, String>> handleFeignConnectionError(RetryableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("message", "No se pudo contactar al servicio de inventario. Inténtelo más tarde."));
    }

    // Puedes agregar más excepciones aquí según el caso
}


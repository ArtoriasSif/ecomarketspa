package com.nebula.msvc_detalle_pedido.exceptions;

import com.nebula.msvc_detalle_pedido.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    private ErrorDTO createErrorDTO(int status, Date date , Map<String,String> errorMap){
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatus(status);
        errorDTO.setDate(date);
        errorDTO.setErrors(errorMap);

        return errorDTO;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationField(MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        for (FieldError fieldErrorDTO : ex.getBindingResult().getFieldErrors()){
            errorMap.put(fieldErrorDTO.getField(),fieldErrorDTO.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(this.createErrorDTO(HttpStatus.BAD_REQUEST.value(),new Date(),errorMap));
    }

    @ExceptionHandler(DetallePedidosException.class)
    public ResponseEntity<ErrorDTO> handleInventoryException(DetallePedidosException ex){
        Map<String,String> errorMap = Collections.singletonMap("inventory",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(this.createErrorDTO(HttpStatus.NOT_FOUND.value(),new Date(),errorMap));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = "JSON mal formado o dato inválido: " + ex.getMostSpecificCause().getMessage();
        Map<String, String> errorMap = Collections.singletonMap("message", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorDTO(HttpStatus.BAD_REQUEST.value(), new Date(), errorMap));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errorMap = Collections.singletonMap("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(this.createErrorDTO(HttpStatus.NOT_FOUND.value(), new Date(), errorMap));
    }




}

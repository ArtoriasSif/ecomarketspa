package com.nebula.msvc_usuarios.exception;

import com.nebula.msvc_usuarios.dto.UsuarioErrorDTO;
import jakarta.validation.ConstraintViolationException;
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


        private UsuarioErrorDTO createErrorDTO(int status, Date date , Map<String,String> errorMap){
            UsuarioErrorDTO usuarioErrorDTO = new UsuarioErrorDTO();

            usuarioErrorDTO.setStatus(status);
            usuarioErrorDTO.setDate(date);
            usuarioErrorDTO.setErrors(errorMap);

            return usuarioErrorDTO;
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<UsuarioErrorDTO> handleValidationField(MethodArgumentNotValidException ex){
            Map<String,String> errorMap = new HashMap<>();
            for (FieldError fieldErrorDTO : ex.getBindingResult().getFieldErrors()){
                errorMap.put(fieldErrorDTO.getField(),fieldErrorDTO.getDefaultMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(this.createErrorDTO(HttpStatus.BAD_REQUEST.value(),new Date(),errorMap));
        }

        @ExceptionHandler(UsuarioException.class)
        public ResponseEntity<UsuarioErrorDTO> handleProductException(UsuarioException ex){
            Map<String,String> errorMap = Collections.singletonMap("usuario",ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(this.createErrorDTO(HttpStatus.NOT_FOUND.value(),new Date(),errorMap));
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<UsuarioErrorDTO> handleConstraintViolation(ConstraintViolationException ex) {
            Map<String, String> errorMap = new HashMap<>();

            ex.getConstraintViolations().forEach(cv -> {
                String field = cv.getPropertyPath().toString();
                String message = cv.getMessage();
                errorMap.put(field, message);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(this.createErrorDTO(HttpStatus.BAD_REQUEST.value(), new Date(), errorMap));
        }

}

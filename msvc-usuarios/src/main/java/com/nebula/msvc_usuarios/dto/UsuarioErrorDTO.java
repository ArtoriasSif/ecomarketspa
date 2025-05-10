package com.nebula.msvc_usuarios.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
@Getter
@Setter
public class UsuarioErrorDTO {
    private Integer status;
    private Date date;
    private Map<String, String> errors;

        @Override
        public String toString() {
            return "ErrorDTO{" +
                    "status=" + status +
                    ", date=" + date +
                    ", errors=" + errors +
                    '}';
        }

}

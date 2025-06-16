package com.cmunoz.msvc.sucursal.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class SucursalDTO {

    @JsonIgnore
    private Long idSucursal;
    private Integer status;
    private Date date;
    private Map<String, String> errors;

    @Override
    public String toString() {
        return "SucursalDTO{" +
                "status=" + status +
                ", date=" + date +
                ", errors=" + errors +
                '}';
    }
}

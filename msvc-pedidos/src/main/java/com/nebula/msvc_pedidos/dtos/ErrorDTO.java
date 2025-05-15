package com.nebula.msvc_pedidos.dtos;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorDTO {
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

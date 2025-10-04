package com.danrod.backend.franchiseinventory.application.dtos;


import lombok.Getter;
import lombok.Setter;


/**
 * DTO para representar errores en las respuestas de la API.
 * Contiene un código de error y detalles adicionales.
 */
@Getter
@Setter
public class ApiErrorDTO {

    private String code;
    private Object details;

    public ApiErrorDTO(String code, Object details) {
        this.code = code;
        this.details = details;
    }

}

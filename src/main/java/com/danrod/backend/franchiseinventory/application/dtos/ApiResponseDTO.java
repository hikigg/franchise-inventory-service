package com.danrod.backend.franchiseinventory.application.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    private T data;
    private PaginationDTO pagination;
    private boolean success;
    private String message;
    private ApiErrorDTO error;

    // Constructor privado para forzar el uso de los métodos estáticos
    private ApiResponseDTO(T data, PaginationDTO pagination) {
        this.data = data;
        this.pagination = pagination;
        this.success = true;
    }

    private ApiResponseDTO(Boolean success, String message, ApiErrorDTO error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }

    /**
     * Crea una respuesta exitosa SIN mensaje, solo con data.
     */
    public static <T> ApiResponseDTO<T> ok(T data) {
        return new ApiResponseDTO<>(data, null);
    }

    /**
     * Crea una respuesta exitosa con paginación.
     */
    public static <T> ApiResponseDTO<T> ok(T data, PaginationDTO pagination) {
        return new ApiResponseDTO<>(data, pagination);
    }

    /**
     * Crea una respuesta de error con código de error.
     */
    public static ApiResponseDTO<Void> error(String message, String errorCode) {
        return new ApiResponseDTO<>(false, message, new ApiErrorDTO(errorCode, null));
    }

    /**
     * Crea una respuesta de error con detalles adicionales.
     */
    public static ApiResponseDTO<Void> error(String message, String errorCode, Object details) {
        return new ApiResponseDTO<>(false, message, new ApiErrorDTO(errorCode, details));
    }


}

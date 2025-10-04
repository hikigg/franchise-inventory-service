package com.danrod.backend.franchiseinventory.application.helpers;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseEntityHelper {

    /**
     * Retorna una respuesta de error con código, mensaje y detalles opcionales.
     */
    public static ResponseEntity<ApiResponseDTO<Void>> error(String errorCode, String message, HttpStatus status, Object details) {
        return ResponseEntity
                .status(status)
                .body(ApiResponseDTO.error(message, errorCode, details));
    }

    /**
     * Retorna una respuesta de error sin detalles adicionales.
     */
    public static ResponseEntity<ApiResponseDTO<Void>> error(String errorCode, String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(ApiResponseDTO.error(message, errorCode));
    }
}
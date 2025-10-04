package com.danrod.backend.franchiseinventory.infrastructure.configs;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.helpers.ResponseEntityHelper;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.NotFoundException;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    // Enum para los códigos de error
    private enum GeneralErrorCode {
        UNKNOWN_ERROR,
        NOT_FOUND,
        VALIDATION_ERROR,
        RESOURCE_ALREADY_EXISTS
    }


    /**
     * Maneja excepciones de recursos no encontrados (HTTP 404).
     * Se activa cuando un servicio emite Mono.error(new NotFoundException(...)).
     */
    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ApiResponseDTO<Void>>> handleNotFoundException(NotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return Mono.just(ResponseEntityHelper.error(
                GeneralErrorCode.NOT_FOUND.name(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        ));
    }

    /**
     * Maneja errores de validación de los DTOs de entrada (HTTP 400).
     * Se activa automáticamente cuando un @RequestBody con @Valid falla la validación.
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponseDTO<Void>>> handleValidationException(WebExchangeBindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Error de validación: {}", errors);
        return Mono.just(ResponseEntityHelper.error(
                GeneralErrorCode.VALIDATION_ERROR.name(),
                "La petición contiene datos inválidos.",
                HttpStatus.BAD_REQUEST,
                errors
        ));
    }

    /**
     * Manejador genérico para cualquier otra excepción no controlada (HTTP 500).
     * Este es el "catch-all" para errores inesperados del servidor.
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponseDTO<Void>>> handleGeneralException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        return Mono.just(ResponseEntityHelper.error(
                GeneralErrorCode.UNKNOWN_ERROR.name(),
                "Ocurrió un error inesperado en el servidor.",
                HttpStatus.INTERNAL_SERVER_ERROR
        ));
    }


    /**
     * Maneja excepciones de recursos que ya existen (HTTP 409 Conflict).
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public Mono<ResponseEntity<ApiResponseDTO<Void>>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.warn("Conflicto de recurso: {}", ex.getMessage());
        return Mono.just(ResponseEntityHelper.error(
                GeneralErrorCode.RESOURCE_ALREADY_EXISTS.name(),
                ex.getMessage(),
                HttpStatus.CONFLICT
        ));
    }
}

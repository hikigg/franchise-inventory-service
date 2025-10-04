package com.danrod.backend.franchiseinventory.application.commands;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Comando para manejar la paginación en las solicitudes.
 */
@Data
public class PageableCommand {

    @Parameter(description = "Número de la página a solicitar (inicia en 0).", example = "0")
    @Min(value = 0, message = "El número de página no puede ser negativo.")
    private int page = 0; // Valor por defecto

    @Parameter(description = "Número de elementos por página.", example = "10")
    @Min(value = 1, message = "El tamaño de la página debe ser al menos 1.")
    @Max(value = 500, message = "El tamaño de la página no puede ser mayor a 500.")
    private int size = 10; // Valor por defecto
}
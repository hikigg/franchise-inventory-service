package com.danrod.backend.franchiseinventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Datos de un producto", description = "DTO para la respuesta de un producto creada o consultada.")
public class ProductResponseDTO implements Serializable {

    @Schema(description = "ID del producto.", example = "101")
    private Long id;

    @Schema(description = "Nombre del producto.", example = "Hamburguesa doble carne")
    private String name;

    @Schema(description = "Cantidad en inventario.", example = "50")
    private Integer stock;

    @Schema(description = "ID de la sucursal a la que pertenece.", example = "1")
    private Long branchId;

    @Schema(description = "Fecha de creación.", example = "2025-10-02T11:00:00")
    private LocalDateTime createdAt;

}

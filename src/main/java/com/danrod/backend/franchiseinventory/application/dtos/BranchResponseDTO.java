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
@Schema(name = "Datos de una sucursal", description = "DTO para la respuesta de una sucursal creada o consultada.")
public class BranchResponseDTO implements Serializable {

    @Schema(description = "ID de la sucursal.", example = "101")
    private Long id;

    @Schema(description = "Nombre de la sucursal.", example = "Sucursal Centro")
    private String name;

    @Schema(description = "ID de la franquicia a la que pertenece.", example = "1")
    private Long franchiseId;

    @Schema(description = "Fecha de creación.", example = "2025-10-02T11:00:00")
    private LocalDateTime createdAt;

}

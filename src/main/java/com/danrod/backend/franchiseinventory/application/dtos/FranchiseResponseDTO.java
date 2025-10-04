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
@Schema(name = "Datos de una franquicia", description = "DTO para la respuesta de una franquicia creada o consultada.")
public class FranchiseResponseDTO implements Serializable {

    @Schema(description = "ID único de la franquicia.", example = "1")
    private Long id;

    @Schema(description = "Nombre de la franquicia.", example = "Super Burger")
    private String name;

    @Schema(description = "Fecha y hora de creación de la franquicia.", example = "2025-10-02T10:30:00")
    private LocalDateTime createdAt;

}

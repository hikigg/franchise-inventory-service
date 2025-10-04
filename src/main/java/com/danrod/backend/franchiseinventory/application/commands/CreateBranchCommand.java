package com.danrod.backend.franchiseinventory.application.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Comando para crear una sucursal", description = "Datos necesarios para crear una nueva sucursal.")
public class CreateBranchCommand {

    @Schema(description = "Nombre de la sucursal.", example = "Sucursal Centro", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String name;

    @Schema(description = "ID de la franquicia a la que pertenece la sucursal.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la franquicia es obligatorio.")
    @Positive(message = "El ID de la franquicia debe ser un número positivo.")
    private Long franchiseId;

}

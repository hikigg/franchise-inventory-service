package com.danrod.backend.franchiseinventory.application.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Comando para actualizar el nombre de una franquicia")
public class UpdateFranchiseNameCommand {

    @Schema(description = "El nuevo nombre único de la franquicia.", example = "Burger Master")
    @NotBlank(message = "El nombre de la franquicia no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String name;
}
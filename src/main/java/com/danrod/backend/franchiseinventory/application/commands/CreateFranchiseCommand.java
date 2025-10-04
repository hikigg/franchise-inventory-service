package com.danrod.backend.franchiseinventory.application.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Comando para crear una franquicia", description = "Datos necesarios para crear una nueva franquicia.")
public class CreateFranchiseCommand {

    @Schema(description = "Nombre único de la franquicia.",
            example = "Super Burger")
    @NotBlank(message = "El nombre de la franquicia no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String name;

}

package com.danrod.backend.franchiseinventory.application.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Comando para crear un producto", description = "Datos necesarios para crear un nuevo producto.")
public class CreateProductCommand {

    @Schema(description = "Nombre del producto.", example = "Hamburguesa doble carne", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String name;

    @Schema(description = "Cantidad en inventario.", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El stock es obligatorio.")
    @PositiveOrZero(message = "El stock no puede ser negativo.")
    private Integer stock;

    @Schema(description = "ID de la sucursal a la que pertenece el producto.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la sucursal es obligatorio.")
    @Positive(message = "El ID de la sucursal debe ser un número positivo.")
    private Long branchId;

}

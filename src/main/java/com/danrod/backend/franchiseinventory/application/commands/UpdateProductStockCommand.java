package com.danrod.backend.franchiseinventory.application.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Actualizar Stock de un producto", description = "DTO para actualizar el stock de un producto.")
public class UpdateProductStockCommand {

    @Schema(description = "La nueva cantidad de stock del producto.", example = "150", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El stock no puede ser nulo.")
    @PositiveOrZero(message = "El stock no puede ser un número negativo.")
    private Integer stock;
}

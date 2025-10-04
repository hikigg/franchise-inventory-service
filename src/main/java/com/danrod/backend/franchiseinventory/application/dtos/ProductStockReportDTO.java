package com.danrod.backend.franchiseinventory.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Producto con mayor stock", description = "DTO que representa el producto con más stock de una sucursal.")
public class ProductStockReportDTO implements Serializable {

    @Schema(description = "ID de la sucursal.", example = "101")
    private Long branchId;

    @Schema(description = "Nombre de la sucursal.", example = "Sucursal Centro")
    private String branchName;

    @Schema(description = "Producto con mayor stock en la sucursal.")
    private ProductResponseDTO product;

}

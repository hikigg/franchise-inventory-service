package com.danrod.backend.franchiseinventory.infrastructure.mappers;

import com.danrod.backend.franchiseinventory.application.commands.CreateProductCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static ProductEntity toEntity(CreateProductCommand command) {
        if (command == null) return null;
        return ProductEntity.builder()
                .name(command.getName())
                .stock(command.getStock())
                .branchId(command.getBranchId())
                .build();
    }

    public static ProductResponseDTO toResponse(ProductEntity entity) {
        if (entity == null) return null;
        return ProductResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
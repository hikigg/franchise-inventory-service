package com.danrod.backend.franchiseinventory.infrastructure.mappers;

import com.danrod.backend.franchiseinventory.application.commands.CreateFranchiseCommand;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.domain.entities.FranchiseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FranchiseMapper {

    /**
     * Convierte un CreateFranchiseCommand a una FranchiseEntity
     * @param command El comando de creación de franquicia
     * @return La entidad de franquicia correspondiente, o null si el comando es null
     */
    public static FranchiseEntity toEntity(CreateFranchiseCommand command) {
        if (command == null) {
            return null;
        }
        return FranchiseEntity.builder()
                .name(command.getName())
                .build();
    }

    /**
     * Convierte una FranchiseEntity a una FranchiseResponseDTO
     * @param franchise La entidad de franquicia
     * @return El DTO de respuesta de franquicia correspondiente, o null si la entidad es null
     */
    public static FranchiseResponseDTO toResponse(FranchiseEntity franchise) {
        if (franchise == null) {
            return null;
        }
        return FranchiseResponseDTO.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .createdAt(franchise.getCreatedAt())
                .build();
    }
}
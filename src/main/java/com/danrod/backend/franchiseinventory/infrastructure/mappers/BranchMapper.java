package com.danrod.backend.franchiseinventory.infrastructure.mappers;

import com.danrod.backend.franchiseinventory.application.commands.CreateBranchCommand;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchMapper {

    public static BranchEntity toEntity(CreateBranchCommand command) {
        if (command == null) return null;
        return BranchEntity.builder()
                .name(command.getName())
                .franchiseId(command.getFranchiseId())
                .build();
    }

    public static BranchResponseDTO toResponse(BranchEntity entity) {
        if (entity == null) return null;
        return BranchResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
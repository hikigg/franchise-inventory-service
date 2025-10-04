package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.CreateBranchCommand;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateBranchUseCase {

    private final IBranchService branchService;
    private final IFranchiseService franchiseService; // Necesitamos verificar que la franquicia exista

    @Transactional
    public Mono<BranchResponseDTO> execute(CreateBranchCommand command) {
        // Flujo reactivo con validación
        return franchiseService.findById(command.getFranchiseId()) // 1. Verifica que la franquicia exista
                .then(Mono.defer(() -> branchService.validateExistsByName(command.getName()))) // 2. Valida que el nombre de la sucursal no esté duplicado (puedes mejorar esta validación)
                .then(Mono.fromCallable(() -> BranchMapper.toEntity(command))) // 3. Convierte el DTO a entidad
                .flatMap(branchService::save) // 4. Guarda la nueva sucursal
                .map(BranchMapper::toResponse); // 5. Mapea el resultado a DTO
    }
}
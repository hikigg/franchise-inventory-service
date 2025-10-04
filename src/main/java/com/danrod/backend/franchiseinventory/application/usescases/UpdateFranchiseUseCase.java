package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.UpdateFranchiseNameCommand;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateFranchiseUseCase {

    private final IFranchiseService franchiseService;

    @Transactional
    public Mono<FranchiseResponseDTO> execute(Long franchiseId, UpdateFranchiseNameCommand command) {
        // 1. Encontrar la franquicia. Si no existe, findById arrojará NotFoundException.
        return franchiseService.findById(franchiseId)
                .flatMap(franchise -> {
                    // 2. Si el nombre nuevo es igual al actual, no hacer nada y devolver la entidad.
                    if (franchise.getName().equals(command.getName())) {
                        return Mono.just(franchise);
                    }

                    // 3. Si el nombre es diferente, validar que no exista ya en otra franquicia.
                    return franchiseService.validateExistsByName(command.getName())
                            .then(Mono.fromRunnable(() -> franchise.setName(command.getName())))
                            .thenReturn(franchise);
                })
                // 4. Guardar la entidad (modificada o no) y mapear a la respuesta.
                .flatMap(franchiseService::save)
                .map(FranchiseMapper::toResponse);
    }
}
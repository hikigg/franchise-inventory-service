package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.CreateFranchiseCommand;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final IFranchiseService franchiseService;

    /**
     * Crea una nueva franquicia.
     * Valida que no exista una franquicia con el mismo nombre antes de crearla.
     * @param command Comando que contiene los datos necesarios para crear la franquicia.
     * @return Un Mono que emite el DTO de respuesta de la franquicia creada.
     */
    @Transactional
    public Mono<FranchiseResponseDTO> execute(CreateFranchiseCommand command) {
        // La lógica se compone en una cadena reactiva
        return franchiseService.validateExistsByName(command.getName())
                .then(Mono.fromCallable(() -> FranchiseMapper.toEntity(command)))
                .flatMap(franchiseService::save)
                .map(FranchiseMapper::toResponse);
    }

}

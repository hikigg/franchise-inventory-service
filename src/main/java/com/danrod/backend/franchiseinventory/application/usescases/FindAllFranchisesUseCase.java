package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.PaginationDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.FranchiseEntity;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.FranchiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllFranchisesUseCase {

    private final IFranchiseService franchiseService;

    @Transactional(readOnly = true)
    public Mono<ApiResponseDTO<List<FranchiseResponseDTO>>> execute(int page, int size) {
        return franchiseService.findAllPaginated(page, size)
                .map(tuple -> {
                    // El resultado de Mono.zip es una tupla (Tuple2)
                    List<FranchiseEntity> franchises = tuple.getT1();
                    Long totalItems = tuple.getT2();

                    // Mapea la lista de entidades a una lista de DTOs
                    List<FranchiseResponseDTO> responseData = franchises.stream()
                            .map(FranchiseMapper::toResponse)
                            .toList();

                    // Crea el objeto de paginación
                    int totalPages = (int) Math.ceil((double) totalItems / size);
                    PaginationDTO pagination = new PaginationDTO(page, totalPages, totalItems);

                    // Crea la respuesta final
                    return ApiResponseDTO.ok(responseData, pagination);
                });
    }
}

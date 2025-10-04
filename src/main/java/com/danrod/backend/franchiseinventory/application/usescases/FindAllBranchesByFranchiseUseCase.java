package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.PaginationDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllBranchesByFranchiseUseCase {

    private final IBranchService branchService;
    private final IFranchiseService franchiseService; // Para validar que la franquicia exista

    @Transactional(readOnly = true)
    public Mono<ApiResponseDTO<List<BranchResponseDTO>>> execute(Long franchiseId, int page, int size) {
        // 1. Validar que la franquicia exista
        return franchiseService.findById(franchiseId)
                .then(Mono.defer(() -> branchService.findByFranchiseIdPaginated(franchiseId, page, size)))                .map(tuple -> {
                    List<BranchEntity> branches = tuple.getT1();
                    Long totalItems = tuple.getT2();

                    List<BranchResponseDTO> responseData = branches.stream()
                            .map(BranchMapper::toResponse)
                            .toList();

                    int totalPages = (int) Math.ceil((double) totalItems / size);
                    PaginationDTO pagination = new PaginationDTO(page, totalPages, totalItems);

                    return ApiResponseDTO.ok(responseData, pagination);
                });
    }
}
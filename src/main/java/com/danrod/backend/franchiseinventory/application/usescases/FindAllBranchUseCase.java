package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.PaginationDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllBranchUseCase {

    private final IBranchService branchService;

    @Transactional(readOnly = true)
    public Mono<ApiResponseDTO<List<BranchResponseDTO>>> execute(int page, int size) {
        return branchService.findAllPaginated(page, size)
                .map(tuple -> {
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
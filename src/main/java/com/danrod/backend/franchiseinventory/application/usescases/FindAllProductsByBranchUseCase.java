package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.PaginationDTO;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllProductsByBranchUseCase {

    private final IProductService productService;
    private final IBranchService branchService; // Para validar que la sucursal exista

    @Transactional(readOnly = true)
    public Mono<ApiResponseDTO<List<ProductResponseDTO>>> execute(Long branchId, int page, int size) {
        // 1. Validar que la sucursal exista primero
        return branchService.findById(branchId)
                .then(productService.findByBranchIdPaginated(branchId, page, size))
                .map(tuple -> {
                    List<ProductEntity> products = tuple.getT1();
                    Long totalItems = tuple.getT2();

                    List<ProductResponseDTO> responseData = products.stream()
                            .map(ProductMapper::toResponse)
                            .toList();

                    int totalPages = (int) Math.ceil((double) totalItems / size);
                    PaginationDTO pagination = new PaginationDTO(page, totalPages, totalItems);

                    return ApiResponseDTO.ok(responseData, pagination);
                });
    }
}
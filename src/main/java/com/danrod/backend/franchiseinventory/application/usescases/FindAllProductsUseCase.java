package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.PaginationDTO;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
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
public class FindAllProductsUseCase {

    private final IProductService productService;

    @Transactional(readOnly = true)
    public Mono<ApiResponseDTO<List<ProductResponseDTO>>> execute(int page, int size) {
        return productService.findAllPaginated(page, size) // Se usa el método paginado
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
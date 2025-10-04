package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ProductStockReportDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.ProductMapper;
import com.danrod.backend.franchiseinventory.infrastructure.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class FindTopStockByFranchiseUseCase {


    private final IBranchService branchService;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Flux<ProductStockReportDTO> execute(Long franchiseId) {
        return branchService.findByFranchiseId(franchiseId)
                .flatMap(branch ->
                        productRepository.findTopByBranchIdOrderByStockDesc(branch.getId())
                                .map(product -> ProductStockReportDTO.builder()
                                        .branchId(branch.getId())
                                        .branchName(branch.getName())
                                        .product(ProductMapper.toResponse(product))
                                        .build()));

    }

}

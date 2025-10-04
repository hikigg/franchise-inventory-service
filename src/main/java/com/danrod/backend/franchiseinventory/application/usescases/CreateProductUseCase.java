package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.CreateProductCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final IProductService productService;
    private final IBranchService branchService;

    @Transactional
    public Mono<ProductResponseDTO> execute(CreateProductCommand command) {
        return branchService.findById(command.getBranchId()) // 1. Valida que la sucursal exista
                .then(Mono.fromCallable(() -> ProductMapper.toEntity(command))) // 2. Convierte a entidad
                .flatMap(productService::save) // 3. Guarda el producto
                .map(ProductMapper::toResponse); // 4. Mapea a DTO de respuesta
    }
}
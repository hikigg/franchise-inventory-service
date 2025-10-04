package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.UpdateProductStockCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.infrastructure.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final IProductService productService;

    @Transactional
    public Mono<ProductResponseDTO> execute(Long productId, UpdateProductStockCommand command) {
        return productService.findById(productId) // 1. Encuentra el producto. Si no existe, el servicio arrojará un NotFoundException.
                .flatMap(product -> {
                    product.setStock(command.getStock()); // 2. Actualiza el campo de stock.
                    return productService.save(product);  // 3. Guarda la entidad actualizada.
                })
                .map(ProductMapper::toResponse); // 4. Mapea la entidad guardada a la respuesta DTO.
    }
}
package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final IProductService productService;

    @Transactional
    public Mono<Void> execute(Long id) {
        // changeActive ya implementa la lógica de borrado lógico
        return productService.changeActive(id, false);
    }
}
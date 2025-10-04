package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.CreateProductCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private IProductService productService;

    @Mock
    private IBranchService branchService;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private CreateProductCommand command;
    private BranchEntity existingBranch;
    private ProductEntity newProductEntity;

    @BeforeEach
    void setUp() {
        command = CreateProductCommand.builder()
                .name("Hamburguesa Doble")
                .stock(100)
                .branchId(1L)
                .build();

        existingBranch = BranchEntity.builder()
                .id(1L)
                .name("Sucursal Centro")
                .franchiseId(10L)
                .build();

        newProductEntity = ProductEntity.builder()
                .id(501L)
                .name("Hamburguesa Doble")
                .stock(100)
                .branchId(1L)
                .build();
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // --- ARRANGE ---
        // 1. Simular que la sucursal SÍ existe
        when(branchService.findById(command.getBranchId())).thenReturn(Mono.just(existingBranch));
        // 2. Simular que el guardado del producto es exitoso
        when(productService.save(any(ProductEntity.class))).thenReturn(Mono.just(newProductEntity));

        // --- ACT ---
        Mono<ProductResponseDTO> result = createProductUseCase.execute(command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("Hamburguesa Doble") &&
                                response.getStock() == 100 &&
                                response.getBranchId().equals(1L))
                .verifyComplete();

        // Verificar que ambos pasos de la cadena reactiva fueron llamados
        verify(branchService).findById(1L);
        verify(productService).save(any(ProductEntity.class));
    }

    @Test
    void shouldReturnErrorWhenBranchNotFound() {
        // --- ARRANGE ---
        // 1. Simular que la sucursal NO existe, devolviendo un error
        when(branchService.findById(anyLong())).thenReturn(Mono.error(new NotFoundException("Sucursal", "1")));

        // --- ACT ---
        Mono<ProductResponseDTO> result = createProductUseCase.execute(command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        // Verificar que, como la sucursal no se encontró, el método save NUNCA se ejecutó
        verify(productService, never()).save(any(ProductEntity.class));
    }
}
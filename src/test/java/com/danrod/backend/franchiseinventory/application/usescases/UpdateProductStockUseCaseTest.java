package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.UpdateProductStockCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductStockUseCaseTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private UpdateProductStockUseCase updateProductStockUseCase;

    private UpdateProductStockCommand command;
    private ProductEntity existingProduct;
    private ProductEntity updatedProduct;
    private final Long productId = 1L;

    @BeforeEach
    void setUp() {
        command = new UpdateProductStockCommand(150);

        existingProduct = ProductEntity.builder()
                .id(productId)
                .name("Hamburguesa Clásica")
                .stock(50)
                .branchId(10L)
                .build();

        updatedProduct = ProductEntity.builder()
                .id(productId)
                .name("Hamburguesa Clásica")
                .stock(150)
                .branchId(10L)
                .build();
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        // --- ARRANGE ---
        // 1. Simular que el producto SÍ se encuentra
        when(productService.findById(productId)).thenReturn(Mono.just(existingProduct));
        // 2. Simular que el guardado del producto actualizado es exitoso
        when(productService.save(any(ProductEntity.class))).thenReturn(Mono.just(updatedProduct));

        // --- ACT ---
        Mono<ProductResponseDTO> result = updateProductStockUseCase.execute(productId, command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStock() == 150)
                .verifyComplete();

        // 3. (Opcional pero recomendado) Capturar el argumento para verificar que se guardó la entidad correcta
        ArgumentCaptor<ProductEntity> productCaptor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productService).save(productCaptor.capture());

        // Verificar que el stock de la entidad que se pasó al método save es el correcto
        assertThat(productCaptor.getValue().getStock()).isEqualTo(150);
        assertThat(productCaptor.getValue().getId()).isEqualTo(productId);
    }

    @Test
    void shouldReturnErrorWhenProductNotFound() {
        // --- ARRANGE ---
        // 1. Simular que el producto NO se encuentra
        when(productService.findById(anyLong())).thenReturn(Mono.error(new NotFoundException("Producto", "1")));

        // --- ACT ---
        Mono<ProductResponseDTO> result = updateProductStockUseCase.execute(productId, command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        // Verificar que el método save NUNCA fue invocado, ya que no se encontró el producto a actualizar
        verify(productService, never()).save(any(ProductEntity.class));
    }
}
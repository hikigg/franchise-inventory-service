package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllProductsUseCaseTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private FindAllProductsUseCase findAllProductsUseCase;

    @Test
    void shouldReturnPaginatedProductsSuccessfully() {
        // --- ARRANGE ---
        // 1. Crear datos de prueba: una lista de productos y un conteo total
        List<ProductEntity> products = List.of(
                ProductEntity.builder().id(1L).name("Producto A").stock(10).build(),
                ProductEntity.builder().id(2L).name("Producto B").stock(20).build()
        );
        long totalItems = 27L; // Imaginemos que hay 27 productos en total en la BD
        int page = 0;
        int size = 10;

        // 2. Simular la respuesta del servicio: devolver la lista y el conteo en una Tupla
        when(productService.findAllPaginated(page, size))
                .thenReturn(Mono.just(Tuples.of(products, totalItems)));

        // --- ACT ---
        Mono<ApiResponseDTO<List<ProductResponseDTO>>> result = findAllProductsUseCase.execute(page, size);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(apiResponse -> {
                    // Verificar el contenido de los datos
                    boolean dataMatches = apiResponse.getData().size() == 2 &&
                            apiResponse.getData().get(0).getName().equals("Producto A");
                    // Verificar el objeto de paginación
                    boolean paginationMatches = apiResponse.getPagination().getCurrentPage() == page &&
                            apiResponse.getPagination().getTotalItems() == totalItems &&
                            apiResponse.getPagination().getTotalPages() == 3; // 27 items / 10 por página = 2.7, redondeado hacia arriba es 3

                    return dataMatches && paginationMatches;
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyResponseWhenNoProductsFound() {
        // --- ARRANGE ---
        // 1. Simular que el servicio no devuelve productos
        List<ProductEntity> emptyList = List.of();
        long totalItems = 0L;
        int page = 0;
        int size = 10;

        when(productService.findAllPaginated(page, size))
                .thenReturn(Mono.just(Tuples.of(emptyList, totalItems)));

        // --- ACT ---
        Mono<ApiResponseDTO<List<ProductResponseDTO>>> result = findAllProductsUseCase.execute(page, size);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(apiResponse ->
                        apiResponse.getData().isEmpty() &&
                                apiResponse.getPagination().getTotalItems() == 0 &&
                                apiResponse.getPagination().getTotalPages() == 0)
                .verifyComplete();
    }
}
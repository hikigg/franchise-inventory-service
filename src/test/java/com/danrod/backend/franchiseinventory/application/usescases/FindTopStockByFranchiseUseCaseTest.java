package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ProductStockReportDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import com.danrod.backend.franchiseinventory.infrastructure.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTopStockByFranchiseUseCaseTest {

    @Mock
    private IBranchService branchService;

    @Mock
    private ProductRepository productRepository; // Inyectamos el repositorio directamente como en el caso de uso

    @InjectMocks
    private FindTopStockByFranchiseUseCase findTopStockByFranchiseUseCase;

    private BranchEntity branch1;
    private BranchEntity branch2;
    private ProductEntity topProductBranch1;
    private ProductEntity topProductBranch2;
    private final Long franchiseId = 1L;

    @BeforeEach
    void setUp() {
        // Sucursal 1 y su producto con más stock
        branch1 = BranchEntity.builder().id(10L).name("Sucursal Norte").franchiseId(franchiseId).build();
        topProductBranch1 = ProductEntity.builder().id(101L).name("Pizza Familiar").stock(50).branchId(10L).build();

        // Sucursal 2 y su producto con más stock
        branch2 = BranchEntity.builder().id(11L).name("Sucursal Sur").franchiseId(franchiseId).build();
        topProductBranch2 = ProductEntity.builder().id(201L).name("Papas Grandes").stock(120).branchId(11L).build();
    }

    @Test
    void shouldReturnTopStockProductForEachBranch() {
        // --- ARRANGE ---
        // 1. Cuando se busquen las sucursales de la franquicia, devolver nuestras dos sucursales de prueba
        when(branchService.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branch1, branch2));

        // 2. Para CADA sucursal, definir cuál es el producto top que el repositorio debe devolver
        when(productRepository.findTopByBranchIdOrderByStockDesc(branch1.getId())).thenReturn(Mono.just(topProductBranch1));
        when(productRepository.findTopByBranchIdOrderByStockDesc(branch2.getId())).thenReturn(Mono.just(topProductBranch2));

        // --- ACT ---
        Flux<ProductStockReportDTO> result = findTopStockByFranchiseUseCase.execute(franchiseId);

        // --- ASSERT ---
        StepVerifier.create(result)
                // Esperamos dos resultados, uno por cada sucursal
                .expectNextCount(2)
                .thenConsumeWhile(report -> true, report -> { // Consumir y verificar cada item
                    if (report.getBranchId().equals(branch1.getId())) {
                        assert report.getProduct().getName().equals("Pizza Familiar");
                        assert report.getProduct().getStock() == 50;
                    } else if (report.getBranchId().equals(branch2.getId())) {
                        assert report.getProduct().getName().equals("Papas Grandes");
                        assert report.getProduct().getStock() == 120;
                    } else {
                        throw new AssertionError("Report de sucursal inesperado");
                    }
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenFranchiseHasNoBranches() {
        // --- ARRANGE ---
        // Simular que la franquicia no tiene sucursales
        when(branchService.findByFranchiseId(franchiseId)).thenReturn(Flux.empty());

        // --- ACT ---
        Flux<ProductStockReportDTO> result = findTopStockByFranchiseUseCase.execute(franchiseId);

        // --- ASSERT ---
        StepVerifier.create(result)
                // Verificar que no se emite ningún resultado
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void shouldReturnOnlyReportsForBranchesWithProducts() {
        // --- ARRANGE ---
        // Sucursal 3, que no tiene productos
        BranchEntity branch3 = BranchEntity.builder().id(12L).name("Sucursal Nueva").franchiseId(franchiseId).build();

        // 1. Devolver tres sucursales
        when(branchService.findByFranchiseId(franchiseId)).thenReturn(Flux.just(branch1, branch2, branch3));

        // 2. Definir los productos top para las dos primeras
        when(productRepository.findTopByBranchIdOrderByStockDesc(branch1.getId())).thenReturn(Mono.just(topProductBranch1));
        when(productRepository.findTopByBranchIdOrderByStockDesc(branch2.getId())).thenReturn(Mono.just(topProductBranch2));
        // 3. Para la tercera sucursal, el repositorio devuelve un Mono vacío (no encontró productos)
        when(productRepository.findTopByBranchIdOrderByStockDesc(branch3.getId())).thenReturn(Mono.empty());

        // --- ACT ---
        Flux<ProductStockReportDTO> result = findTopStockByFranchiseUseCase.execute(franchiseId);

        // --- ASSERT ---
        StepVerifier.create(result)
                // Verificar que solo se emiten 2 resultados, ignorando la sucursal sin productos
                .expectNextCount(2)
                .verifyComplete();
    }
}
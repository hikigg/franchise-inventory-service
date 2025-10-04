package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import com.danrod.backend.franchiseinventory.domain.entities.FranchiseEntity;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuples;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllBranchesByFranchiseUseCaseTest {

    @Mock
    private IBranchService branchService;

    @Mock
    private IFranchiseService franchiseService;

    @InjectMocks
    private FindAllBranchesByFranchiseUseCase findAllBranchesByFranchiseUseCase;

    @Test
    void shouldReturnPaginatedBranchesForAFranchise() {
        // --- ARRANGE ---
        long franchiseId = 1L;
        int page = 0;
        int size = 5;
        long totalItems = 12L; // 12 sucursales en total para esta franquicia

        FranchiseEntity existingFranchise = FranchiseEntity.builder().id(franchiseId).name("Franquicia de Prueba").build();
        List<BranchEntity> branches = List.of(
                BranchEntity.builder().id(10L).name("Sucursal A").franchiseId(franchiseId).build(),
                BranchEntity.builder().id(11L).name("Sucursal B").franchiseId(franchiseId).build()
        );

        // 1. Simular que la franquicia SÍ existe
        when(franchiseService.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        // 2. Simular la respuesta paginada del servicio de sucursales
        when(branchService.findByFranchiseIdPaginated(franchiseId, page, size))
                .thenReturn(Mono.just(Tuples.of(branches, totalItems)));

        // --- ACT ---
        Mono<ApiResponseDTO<List<BranchResponseDTO>>> result = findAllBranchesByFranchiseUseCase.execute(franchiseId, page, size);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(apiResponse -> {
                    boolean dataMatches = apiResponse.getData().size() == 2 &&
                            apiResponse.getData().get(0).getName().equals("Sucursal A");

                    boolean paginationMatches = apiResponse.getPagination().getCurrentPage() == page &&
                            apiResponse.getPagination().getTotalItems() == totalItems &&
                            apiResponse.getPagination().getTotalPages() == 3; // ceil(12.0 / 5.0) = 3

                    return dataMatches && paginationMatches;
                })
                .verifyComplete();

        // Verificar que ambos servicios fueron llamados
        verify(franchiseService).findById(franchiseId);
        verify(branchService).findByFranchiseIdPaginated(franchiseId, page, size);
    }

    @Test
    void shouldReturnErrorWhenFranchiseForFilteringIsNotFound() {
        // --- ARRANGE ---
        long nonExistentFranchiseId = 99L;
        int page = 0;
        int size = 5;

        // 1. Simular que la franquicia NO se encuentra
        when(franchiseService.findById(nonExistentFranchiseId))
                .thenReturn(Mono.error(new NotFoundException("Franquicia", String.valueOf(nonExistentFranchiseId))));

        // --- ACT ---
        Mono<ApiResponseDTO<List<BranchResponseDTO>>> result = findAllBranchesByFranchiseUseCase.execute(nonExistentFranchiseId, page, size);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        // Verificar que, como la franquicia no existe, nunca se intentó buscar sus sucursales
        verify(branchService, never()).findByFranchiseIdPaginated(anyLong(), anyInt(), anyInt());
    }
}
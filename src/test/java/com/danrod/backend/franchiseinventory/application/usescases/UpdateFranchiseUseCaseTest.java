package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.UpdateFranchiseNameCommand;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.FranchiseEntity;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.NotFoundException;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.ResourceAlreadyExistsException;
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
class UpdateFranchiseUseCaseTest {

    @Mock
    private IFranchiseService franchiseService;

    @InjectMocks
    private UpdateFranchiseUseCase updateFranchiseNameUseCase;

    private FranchiseEntity existingFranchise;
    private final Long franchiseId = 1L;

    @BeforeEach
    void setUp() {
        existingFranchise = FranchiseEntity.builder()
                .id(franchiseId)
                .name("Super Burger")
                .build();
    }

    @Test
    void shouldUpdateFranchiseNameSuccessfully() {
        // --- ARRANGE ---
        UpdateFranchiseNameCommand command = new UpdateFranchiseNameCommand("Burger Master");
        FranchiseEntity updatedFranchise = FranchiseEntity.builder().id(franchiseId).name("Burger Master").build();

        when(franchiseService.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        when(franchiseService.validateExistsByName("Burger Master")).thenReturn(Mono.empty());
        when(franchiseService.save(any(FranchiseEntity.class))).thenReturn(Mono.just(updatedFranchise));

        // --- ACT ---
        Mono<FranchiseResponseDTO> result = updateFranchiseNameUseCase.execute(franchiseId, command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getName().equals("Burger Master"))
                .verifyComplete();

        verify(franchiseService).findById(franchiseId);
        verify(franchiseService).validateExistsByName("Burger Master");
        verify(franchiseService).save(any(FranchiseEntity.class));
    }

    @Test
    void shouldDoNothingIfNameIsTheSame() {
        // --- ARRANGE ---
        UpdateFranchiseNameCommand commandWithSameName = new UpdateFranchiseNameCommand("Super Burger");

        // El save devolverá la misma entidad porque no hay cambios
        when(franchiseService.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        when(franchiseService.save(any(FranchiseEntity.class))).thenReturn(Mono.just(existingFranchise));


        // --- ACT ---
        Mono<FranchiseResponseDTO> result = updateFranchiseNameUseCase.execute(franchiseId, commandWithSameName);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(response -> response.getName().equals("Super Burger"))
                .verifyComplete();

        // Verificar que findById se llamó, pero la validación y el guardado (real de cambio) no fueron necesarios.
        verify(franchiseService).findById(franchiseId);
        // La validación de nombre duplicado no debería ejecutarse si el nombre no cambia.
        verify(franchiseService, never()).validateExistsByName(anyString());
        // El save se llama al final del flujo, pero lo importante es que no se validó.
        verify(franchiseService).save(any(FranchiseEntity.class));
    }

    @Test
    void shouldReturnErrorWhenNewNameAlreadyExists() {
        // --- ARRANGE ---
        UpdateFranchiseNameCommand command = new UpdateFranchiseNameCommand("Burger Master");

        when(franchiseService.findById(franchiseId)).thenReturn(Mono.just(existingFranchise));
        when(franchiseService.validateExistsByName("Burger Master"))
                .thenReturn(Mono.error(new ResourceAlreadyExistsException("Franquicia", "Burger Master")));

        // --- ACT ---
        Mono<FranchiseResponseDTO> result = updateFranchiseNameUseCase.execute(franchiseId, command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(ResourceAlreadyExistsException.class)
                .verify();

        verify(franchiseService).findById(franchiseId);
        verify(franchiseService, never()).save(any(FranchiseEntity.class));
    }

    @Test
    void shouldReturnErrorWhenFranchiseToUpdateIsNotFound() {
        // --- ARRANGE ---
        UpdateFranchiseNameCommand command = new UpdateFranchiseNameCommand("Burger Master");
        when(franchiseService.findById(anyLong())).thenReturn(Mono.error(new NotFoundException("Franquicia", "1")));

        // --- ACT ---
        Mono<FranchiseResponseDTO> result = updateFranchiseNameUseCase.execute(franchiseId, command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        verify(franchiseService, never()).validateExistsByName(anyString());
        verify(franchiseService, never()).save(any(FranchiseEntity.class));
    }
}
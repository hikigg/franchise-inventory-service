package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.CreateBranchCommand;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
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
class CreateBranchUseCaseTest {

    @Mock
    private IBranchService branchService;

    @Mock
    private IFranchiseService franchiseService;

    @InjectMocks
    private CreateBranchUseCase createBranchUseCase;

    private CreateBranchCommand command;
    private FranchiseEntity existingFranchise;
    private BranchEntity newBranchEntity;

    @BeforeEach
    void setUp() {
        // Datos de prueba comunes
        command = CreateBranchCommand.builder()
                .name("Sucursal Centro")
                .franchiseId(1L)
                .build();

        existingFranchise = FranchiseEntity.builder()
                .id(1L)
                .name("Super Burger")
                .build();

        newBranchEntity = BranchEntity.builder()
                .id(101L)
                .name("Sucursal Centro")
                .franchiseId(1L)
                .build();
    }

    @Test
    void shouldCreateBranchSuccessfully() {
        // --- ARRANGE ---
        // 1. Simular que la franquicia SÍ existe
        when(franchiseService.findById(command.getFranchiseId())).thenReturn(Mono.just(existingFranchise));
        // 2. Simular que el nombre de la sucursal NO existe
        when(branchService.validateExistsByName(command.getName())).thenReturn(Mono.empty());
        // 3. Simular que el guardado es exitoso
        when(branchService.save(any(BranchEntity.class))).thenReturn(Mono.just(newBranchEntity));

        // --- ACT ---
        Mono<BranchResponseDTO> result = createBranchUseCase.execute(command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getName().equals("Sucursal Centro") && response.getFranchiseId().equals(1L))
                .verifyComplete();

        // Verificar que todos los pasos de la cadena reactiva fueron llamados
        verify(franchiseService).findById(1L);
        verify(branchService).validateExistsByName("Sucursal Centro");
        verify(branchService).save(any(BranchEntity.class));
    }

    @Test
    void shouldReturnErrorWhenFranchiseNotFound() {
        // --- ARRANGE ---
        // 1. Simular que la franquicia NO existe
        when(franchiseService.findById(anyLong())).thenReturn(Mono.error(new NotFoundException("Franquicia", "1")));

        // --- ACT ---
        Mono<BranchResponseDTO> result = createBranchUseCase.execute(command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(NotFoundException.class)
                .verify();

        // Verificar que las validaciones posteriores NUNCA se ejecutaron
        verify(branchService, never()).validateExistsByName(anyString());
        verify(branchService, never()).save(any(BranchEntity.class));
    }

    @Test
    void shouldReturnErrorWhenBranchNameAlreadyExists() {
        // --- ARRANGE ---
        // 1. Simular que la franquicia SÍ existe
        when(franchiseService.findById(command.getFranchiseId())).thenReturn(Mono.just(existingFranchise));
        // 2. Simular que el nombre de la sucursal YA existe
        when(branchService.validateExistsByName(command.getName()))
                .thenReturn(Mono.error(new ResourceAlreadyExistsException("Sucursal", "Sucursal Centro")));

        // --- ACT ---
        Mono<BranchResponseDTO> result = createBranchUseCase.execute(command);

        // --- ASSERT ---
        StepVerifier.create(result)
                .expectError(ResourceAlreadyExistsException.class)
                .verify();

        // Verificar que la franquicia fue verificada pero el guardado NUNCA ocurrió
        verify(franchiseService).findById(1L);
        verify(branchService, never()).save(any(BranchEntity.class));
    }
}
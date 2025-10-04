package com.danrod.backend.franchiseinventory.application.usescases;

import com.danrod.backend.franchiseinventory.application.commands.CreateFranchiseCommand;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.FranchiseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// 1. Usar la extensión de Mockito para JUnit 5
@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    // 2. Crear un "mock" (simulacro) del servicio que es una dependencia.
    @Mock
    private IFranchiseService franchiseService;

    // 3. Inyectar los mocks en la clase que queremos probar.
    @InjectMocks
    private CreateFranchiseUseCase createFranchiseUseCase;

    private CreateFranchiseCommand command;
    private FranchiseEntity franchiseEntity;

    @BeforeEach
    void setUp() {
        // Preparar datos comunes para las pruebas
        command = new CreateFranchiseCommand("Super Burger");

        franchiseEntity = FranchiseEntity.builder()
                .id(1L)
                .name("Super Burger")
                .build();
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {
        // --- ARRANGE (Organizar) ---
        // 4. Definir el comportamiento del mock:
        // Cuando se llame a validateExistsByName con cualquier string, debe devolver un Mono vacío (éxito).
        when(franchiseService.validateExistsByName(anyString())).thenReturn(Mono.empty());
        // Cuando se llame a save con cualquier FranchiseEntity, debe devolver nuestra entidad de prueba.
        when(franchiseService.save(any(FranchiseEntity.class))).thenReturn(Mono.just(franchiseEntity));

        // --- ACT (Actuar) ---
        // Ejecutar el caso de uso.
        Mono<FranchiseResponseDTO> result = createFranchiseUseCase.execute(command);

        // --- ASSERT (Afirmar) ---
        // 5. Usar StepVerifier para probar el resultado del Mono.
        StepVerifier.create(result)
                // Verificar que el DTO de respuesta tiene el nombre correcto.
                .expectNextMatches(responseDTO -> responseDTO.getName().equals("Super Burger"))
                // Verificar que el Mono se completa exitosamente.
                .verifyComplete();

        // 6. Verificar que los métodos del mock fueron llamados como se esperaba.
        verify(franchiseService).validateExistsByName("Super Burger");
        verify(franchiseService).save(any(FranchiseEntity.class));
    }

    @Test
    void shouldReturnErrorWhenFranchiseNameAlreadyExists() {
        // --- ARRANGE (Organizar) ---
        // 7. Simular un error:
        // Cuando se llame a validateExistsByName, devolver un Mono con un error.
        String errorMessage = "Franchise with name 'Super Burger' already exists";
        when(franchiseService.validateExistsByName(command.getName()))
                .thenReturn(Mono.error(new IllegalArgumentException(errorMessage)));

        // --- ACT (Actuar) ---
        Mono<FranchiseResponseDTO> result = createFranchiseUseCase.execute(command);

        // --- ASSERT (Afirmar) ---
        // 8. Verificar que el Mono termina con el error esperado.
        StepVerifier.create(result)
                .expectErrorMessage(errorMessage)
                .verify();

        // 9. Verificar que el método save NUNCA fue llamado, porque la validación falló.
        verify(franchiseService, never()).save(any(FranchiseEntity.class));
    }

}
package com.danrod.backend.franchiseinventory.infrastructure.controllers;

import com.danrod.backend.franchiseinventory.application.commands.CreateFranchiseCommand;
import com.danrod.backend.franchiseinventory.application.commands.PageableCommand;
import com.danrod.backend.franchiseinventory.application.commands.UpdateFranchiseNameCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.FranchiseResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.ProductStockReportDTO;
import com.danrod.backend.franchiseinventory.application.usescases.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises", description = "API para la gestión de franquicias.")
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseUseCase updateFranchiseUseCase; // Inyectar nuevo caso de uso
    private final FindAllFranchisesUseCase findAllFranchisesUseCase; // Inyecta el nuevo caso de uso
    private final FindTopStockByFranchiseUseCase findTopStockByFranchiseUseCase; // Inyecta el nuevo caso de uso
    private final FindAllBranchesByFranchiseUseCase findAllBranchesByFranchiseUseCase; //Inyectar nuevo caso de uso

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Crear una nueva franquicia",
            description = "Endpoint para registrar una nueva franquicia en el sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Franquicia creada exitosamente.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = FranchiseResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Petición inválida (ej. nombre duplicado o vacío).")
            }
    )
    public Mono<ResponseEntity<FranchiseResponseDTO>> createFranchise(@Valid @RequestBody Mono<CreateFranchiseCommand> command) {
        return command
                .flatMap(createFranchiseUseCase::execute)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping
    @Operation(summary = "Listar todas las franquicias de forma paginada",
            responses = @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente."))
    public Mono<ResponseEntity<ApiResponseDTO<List<FranchiseResponseDTO>>>> getAllFranchises(
            @ParameterObject @Valid PageableCommand pageableCommand
    ) {
        return findAllFranchisesUseCase.execute(pageableCommand.getPage(), pageableCommand.getSize())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}/top-stock-products")
    @Operation(summary = "Obtener el producto con más stock por cada sucursal de una franquicia",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente."),
                    @ApiResponse(responseCode = "404", description = "La franquicia no fue encontrada.")
            })
    public Mono<ResponseEntity<List<ProductStockReportDTO>>> getTopStockProductsByFranchise(@PathVariable Long id) {
        return findTopStockByFranchiseUseCase.execute(id)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}/branches")
    @Operation(summary = "Listar sucursales de una franquicia de forma paginada",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente."),
                    @ApiResponse(responseCode = "404", description = "La franquicia no fue encontrada.")
            })
    public Mono<ResponseEntity<ApiResponseDTO<List<BranchResponseDTO>>>> getBranchesByFranchise(
            @PathVariable Long id,
            @ParameterObject @Valid PageableCommand pageableCommand
    ) {
        return findAllBranchesByFranchiseUseCase.execute(id, pageableCommand.getPage(), pageableCommand.getSize())
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar el nombre de una franquicia",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Nombre actualizado exitosamente."),
                    @ApiResponse(responseCode = "400", description = "Petición inválida (ej. nombre vacío)."),
                    @ApiResponse(responseCode = "404", description = "La franquicia no fue encontrada."),
                    @ApiResponse(responseCode = "409", description = "El nuevo nombre ya está en uso por otra franquicia.")
            })
    public Mono<ResponseEntity<FranchiseResponseDTO>> updateFranchiseName(
            @PathVariable Long id,
            @Valid @RequestBody Mono<UpdateFranchiseNameCommand> command) {
        return command
                .flatMap(updateCmd -> updateFranchiseUseCase.execute(id, updateCmd))
                .map(ResponseEntity::ok);
    }

}

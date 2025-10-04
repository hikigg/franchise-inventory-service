package com.danrod.backend.franchiseinventory.infrastructure.controllers;

import com.danrod.backend.franchiseinventory.application.commands.CreateBranchCommand;
import com.danrod.backend.franchiseinventory.application.commands.PageableCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.BranchResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.usescases.CreateBranchUseCase;
import com.danrod.backend.franchiseinventory.application.usescases.FindAllBranchUseCase;
import com.danrod.backend.franchiseinventory.application.usescases.FindAllProductsByBranchUseCase;
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
@RequestMapping("/api/v1/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "API para la gestión de sucursales.")
public class BranchController {

    private final CreateBranchUseCase createBranchUseCase;
    private final FindAllBranchUseCase findAllBranchUseCase;
    private final FindAllProductsByBranchUseCase findAllProductsByBranchUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una nueva sucursal para una franquicia",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Petición inválida"),
                    @ApiResponse(responseCode = "404", description = "La franquicia especificada no existe")
            })
    public Mono<ResponseEntity<BranchResponseDTO>> createBranch(@Valid @RequestBody Mono<CreateBranchCommand> command) {
        return command
                .flatMap(createBranchUseCase::execute)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }


    @GetMapping
    @Operation(
            summary = "Listar todas las sucursales",
            description = "Endpoint para obtener un listado de todas las sucursales activas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BranchResponseDTO.class)))
            }
    )
    public Mono<ResponseEntity<ApiResponseDTO<List<BranchResponseDTO>>>> getAllBranches(
            @ParameterObject @Valid PageableCommand pageableCommand
    ) {
        return findAllBranchUseCase.execute(pageableCommand.getPage(), pageableCommand.getSize())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}/products")
    @Operation(summary = "Listar productos de una sucursal de forma paginada",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado paginado obtenido exitosamente."),
                    @ApiResponse(responseCode = "404", description = "La sucursal no fue encontrada.")
            })
    public Mono<ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>>> getProductsByBranch(
            @PathVariable Long id,
            @ParameterObject @Valid PageableCommand pageableCommand
    ) {
        return findAllProductsByBranchUseCase.execute(id, pageableCommand.getPage(), pageableCommand.getSize())
                .map(ResponseEntity::ok);
    }

}

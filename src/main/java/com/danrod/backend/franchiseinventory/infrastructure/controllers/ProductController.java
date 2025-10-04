package com.danrod.backend.franchiseinventory.infrastructure.controllers;

import com.danrod.backend.franchiseinventory.application.commands.CreateProductCommand;
import com.danrod.backend.franchiseinventory.application.commands.PageableCommand;
import com.danrod.backend.franchiseinventory.application.commands.UpdateProductStockCommand;
import com.danrod.backend.franchiseinventory.application.dtos.ApiResponseDTO;
import com.danrod.backend.franchiseinventory.application.dtos.ProductResponseDTO;
import com.danrod.backend.franchiseinventory.application.usescases.CreateProductUseCase;
import com.danrod.backend.franchiseinventory.application.usescases.DeleteProductUseCase;
import com.danrod.backend.franchiseinventory.application.usescases.FindAllProductsUseCase;
import com.danrod.backend.franchiseinventory.application.usescases.UpdateProductStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API para la gestión de productos en inventario.")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final FindAllProductsUseCase findAllProductsUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear un nuevo producto para una sucursal",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Petición inválida"),
                    @ApiResponse(responseCode = "404", description = "La sucursal especificada no existe")
            })
    public Mono<ResponseEntity<ProductResponseDTO>> createProduct(@Valid @RequestBody Mono<CreateProductCommand> command) {
        return command
                .flatMap(createProductUseCase::execute)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un producto (borrado lógico)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "El producto no fue encontrado")
            })
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
        return deleteProductUseCase.execute(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos",
            responses = @ApiResponse(responseCode = "200", description = "Listado de productos obtenido exitosamente"))
    public Mono<ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>>> getAllProducts(
            @ParameterObject @Valid PageableCommand pageableCommand
    ) {
        return findAllProductsUseCase.execute(pageableCommand.getPage(), pageableCommand.getSize())
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar el stock de un producto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Petición inválida (ej. stock negativo)"),
                    @ApiResponse(responseCode = "404", description = "El producto no fue encontrado")
            })
    public Mono<ResponseEntity<ProductResponseDTO>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody Mono<UpdateProductStockCommand> command) {
        return command
                .flatMap(updateCmd -> updateProductStockUseCase.execute(id, updateCmd))
                .map(ResponseEntity::ok);
    }
}
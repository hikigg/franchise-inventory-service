package com.danrod.backend.franchiseinventory.infrastructure.repositories;

import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends IBaseRepository<ProductEntity, Long> {

    @Override
    @Query("SELECT * FROM products WHERE deleted_at IS NULL ORDER BY name ASC LIMIT :size OFFSET :offset")
    Flux<ProductEntity> findAllPaginated(int size, long offset);

    @Override
    @Query("SELECT COUNT(*) FROM products WHERE deleted_at IS NULL")
    Mono<Long> countAllActive();

    // Sobreescribimos findById para asegurarnos de no encontrar uno borrado lógicamente
    @Query("SELECT * FROM products WHERE id = :id AND deleted_at IS NULL")
    @Override
    Mono<ProductEntity> findById(Long id);

    Mono<Boolean> existsByName(String name);
    
    /**
     * Encuentra todos los productos asociados a una sucursal específica.
     * @param branchId ID de la sucursal.
     * @return Flujo de entidades de productos.
     */
    Flux<ProductEntity> findByBranchId(Long branchId);


    /**
     * Encuentra el producto con el stock más alto para una sucursal específica.
     * Solo considera productos activos (no borrados lógicamente).
     * @param branchId ID de la sucursal.
     * @return Un Mono que emite el producto con el stock más alto, o vacío si no hay productos.
     */
    @Query("SELECT * FROM products WHERE branch_id = :branchId AND deleted_at IS NULL ORDER BY stock DESC LIMIT 1")
    Mono<ProductEntity> findTopByBranchIdOrderByStockDesc(Long branchId);


}

package com.danrod.backend.franchiseinventory.infrastructure.repositories;

import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends IBaseRepository<BranchEntity, Long> {

    @Override
    @Query("SELECT * FROM branches WHERE deleted_at IS NULL ORDER BY name ASC LIMIT :size OFFSET :offset")
    Flux<BranchEntity> findAllPaginated(int size, long offset);

    @Override
    @Query("SELECT COUNT(*) FROM branches WHERE deleted_at IS NULL")
    Mono<Long> countAllActive();

    // Sobreescribimos findById para asegurarnos de no encontrar uno borrado lógicamente
    @Query("SELECT * FROM branches WHERE id = :id AND deleted_at IS NULL")
    @Override
    Mono<BranchEntity> findById(Long id);

    Mono<Boolean> existsByName(String name);

    /**
     * Encuentra sucursales paginadas por ID de franquicia.
     */
    @Query("SELECT * FROM branches WHERE franchise_id = :franchiseId AND deleted_at IS NULL ORDER BY name ASC LIMIT :size OFFSET :offset")
    Flux<BranchEntity> findByFranchiseId(Long franchiseId, int size, long offset);

    /**
     * Cuenta el total de sucursales activas para una franquicia.
     */
    @Query("SELECT COUNT(*) FROM branches WHERE franchise_id = :franchiseId AND deleted_at IS NULL")
    Mono<Long> countByFranchiseId(Long franchiseId);

    /**
     * Encuentra todas las sucursales asociadas a una franquicia específica.
     *
     * @param franchiseId ID de la franquicia.
     * @return Un flujo reactivo de entidades de sucursal.
     */
    Flux<BranchEntity> findByFranchiseId(Long franchiseId);

}

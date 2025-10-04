package com.danrod.backend.franchiseinventory.infrastructure.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface IBaseRepository<T, ID> extends R2dbcRepository<T, ID> {

    /**
     * Encuentra todas las entidades activas de forma paginada.
     * La implementación de la consulta @Query debe estar en la interfaz hija.
     * @param size El número de elementos a traer (LIMIT).
     * @param offset El punto de inicio (OFFSET).
     * @return Un flujo de entidades.
     */
    Flux<T> findAllPaginated(int size, long offset);

    /**
     * Cuenta el total de entidades activas.
     * La implementación de la consulta @Query debe estar en la interfaz hija.
     * @return Un Mono con el conteo total.
     */
    Mono<Long> countAllActive();

    /**
     * Verifica si una entidad existe por su nombre.
     * @param name El nombre a verificar.
     * @return Un Mono que emite true si existe, false en caso contrario.
     */
    Mono<Boolean> existsByName(String name);
}
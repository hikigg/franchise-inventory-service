package com.danrod.backend.franchiseinventory.application.interfaces;

import com.danrod.backend.franchiseinventory.domain.entities.base.BaseEntity;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface IBaseService<T extends BaseEntity> {

    Mono<Void> changeActive(Long id, boolean isActive);

    Mono<Void> delete(Long id);

    Mono<T> findById(Long id);

    Mono<T> save(T entity);

    // La validación también puede ser asíncrona
    Mono<Void> validateExistsByName(String name);

    Mono<Tuple2<List<T>, Long>> findAllPaginated(int page, int size);
}

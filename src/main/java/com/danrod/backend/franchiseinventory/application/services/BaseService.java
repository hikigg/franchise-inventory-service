package com.danrod.backend.franchiseinventory.application.services;

import com.danrod.backend.franchiseinventory.application.interfaces.IBaseService;
import com.danrod.backend.franchiseinventory.domain.entities.base.BaseEntity;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.NotFoundException;
import com.danrod.backend.franchiseinventory.infrastructure.exceptions.ResourceAlreadyExistsException;
import com.danrod.backend.franchiseinventory.infrastructure.repositories.IBaseRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {

    protected final IBaseRepository<T, Long> repository;

    protected abstract String getEntityName();

    protected BaseService(IBaseRepository<T, Long> repository) {
        this.repository = repository;
    }
    @Override
    @Transactional
    public Mono<Void> changeActive(Long id, boolean isActive) {
        // 1. Busca la entidad
        return findById(id)
                // 2. Si se encuentra, entra al flatMap para modificarla
                .flatMap(entity -> {
                    if (entity.isActive() == isActive) {
                        return Mono.empty(); // No hay cambios, no hagas nada
                    }
                    entity.changeActive(isActive);
                    // 3. Guarda la entidad modificada y luego descarta el resultado
                    return repository.save(entity);
                })
                // 4. Ignora el resultado final para devolver Mono<Void>
                .then();
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        // deleteById ya devuelve Mono<Void>
        return repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<T> findById(Long id) {
        return repository.findById(id)
                // Si el Mono está vacío (no se encontró), emite un error
                .switchIfEmpty(Mono.error(new NotFoundException(getEntityName(), id.toString())));
    }

    @Override
    @Transactional
    public Mono<T> save(T entity) {
        // save ya devuelve Mono<T>
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<Void> validateExistsByName(String name) {
        // Ahora con la lógica real
        return repository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        // Si existe, se emite un error con nuestra excepción personalizada.
                        return Mono.error(new ResourceAlreadyExistsException(getEntityName(), name));
                    }
                    // Si no existe, se emite un Mono vacío para indicar éxito.
                    return Mono.empty();
                }).then(); // .then() convierte el Mono<Boolean> en Mono<Void>
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Tuple2<List<T>, Long>> findAllPaginated(int page, int size) {
        long offset = (long) page * size;
        Mono<List<T>> dataMono = repository.findAllPaginated(size, offset).collectList();
        Mono<Long> countMono = repository.countAllActive();
        return Mono.zip(dataMono, countMono);
    }

}

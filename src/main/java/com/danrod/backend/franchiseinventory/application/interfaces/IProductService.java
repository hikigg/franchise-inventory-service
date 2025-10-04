package com.danrod.backend.franchiseinventory.application.interfaces;

import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface IProductService extends IBaseService<ProductEntity> {
    Mono<Tuple2<List<ProductEntity>, Long>> findByBranchIdPaginated(Long branchId, int page, int size);
}

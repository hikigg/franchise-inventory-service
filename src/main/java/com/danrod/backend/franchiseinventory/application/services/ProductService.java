package com.danrod.backend.franchiseinventory.application.services;

import com.danrod.backend.franchiseinventory.application.interfaces.IProductService;
import com.danrod.backend.franchiseinventory.domain.entities.ProductEntity;
import com.danrod.backend.franchiseinventory.infrastructure.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Slf4j
@Service
public class ProductService extends BaseService<ProductEntity> implements IProductService {

    private final ProductRepository productRepository;

    protected ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    @Override
    protected String getEntityName() {
        return "Producto";
    }


}

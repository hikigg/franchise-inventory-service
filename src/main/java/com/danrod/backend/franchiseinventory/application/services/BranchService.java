package com.danrod.backend.franchiseinventory.application.services;

import com.danrod.backend.franchiseinventory.application.interfaces.IBranchService;
import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import com.danrod.backend.franchiseinventory.infrastructure.repositories.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Slf4j
@Service
public class BranchService extends BaseService<BranchEntity> implements IBranchService {

    private final BranchRepository branchRepository;

    protected BranchService(BranchRepository branchRepository) {
        super(branchRepository);
        this.branchRepository = branchRepository;
    }


    @Override
    protected String getEntityName() {
        return "Sucursal";
    }


    @Transactional(readOnly = true)
    @Override
    public Flux<BranchEntity> findByFranchiseId(Long franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Tuple2<List<BranchEntity>, Long>> findByFranchiseIdPaginated(Long franchiseId, int page, int size) {
        long offset = (long) page * size;
        Mono<List<BranchEntity>> dataMono = branchRepository.findByFranchiseId(franchiseId, size, offset).collectList();
        Mono<Long> countMono = branchRepository.countByFranchiseId(franchiseId);
        return Mono.zip(dataMono, countMono);
    }
}

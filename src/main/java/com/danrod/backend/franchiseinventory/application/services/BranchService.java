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



}

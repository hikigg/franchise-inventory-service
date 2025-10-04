package com.danrod.backend.franchiseinventory.application.services;

import com.danrod.backend.franchiseinventory.application.interfaces.IFranchiseService;
import com.danrod.backend.franchiseinventory.domain.entities.FranchiseEntity;
import com.danrod.backend.franchiseinventory.infrastructure.repositories.FranchiseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FranchiseService extends BaseService<FranchiseEntity> implements IFranchiseService {

    protected FranchiseService(FranchiseRepository franchiseRepository) {
        super(franchiseRepository);
    }

    @Override
    protected String getEntityName() {
        return "Franquicia";
    }

}

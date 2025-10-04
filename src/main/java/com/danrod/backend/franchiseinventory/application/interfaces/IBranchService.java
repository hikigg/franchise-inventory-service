package com.danrod.backend.franchiseinventory.application.interfaces;


import com.danrod.backend.franchiseinventory.domain.entities.BranchEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface IBranchService extends IBaseService<BranchEntity> {
}
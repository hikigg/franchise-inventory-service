package com.danrod.backend.franchiseinventory.domain.entities;

import com.danrod.backend.franchiseinventory.domain.entities.base.BaseNameEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Entidad de franquicias
 * EL nombre de la franquicia se implementa en BaseNameEntity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "franchises")
public class FranchiseEntity extends BaseNameEntity implements Serializable {

    @Transient
    private List<BranchEntity> branches; // Sucursales asociadas a la franquicia

}

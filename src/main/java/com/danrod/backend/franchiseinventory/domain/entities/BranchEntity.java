package com.danrod.backend.franchiseinventory.domain.entities;

import com.danrod.backend.franchiseinventory.domain.entities.base.BaseNameEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Entidad que representa una sucursal de una franquicia.
 * Extiende de BaseNameEntity para heredar propiedades comunes como id y name.
 * Contiene una referencia a la franquicia a la que pertenece y una lista de productos asociados.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "branches")
public class BranchEntity extends BaseNameEntity implements Serializable {

    @Column("franchise_id")
    private Long franchiseId; // Id de la franquicia a la que pertenece la sucursal

    @Transient
    private List<ProductEntity> products; // Productos asociados a la sucursal

}

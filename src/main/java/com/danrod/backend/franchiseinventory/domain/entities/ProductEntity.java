package com.danrod.backend.franchiseinventory.domain.entities;

import com.danrod.backend.franchiseinventory.domain.entities.base.BaseNameEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

/**
 * Entidad que representa un producto en el inventario de una sucursal.
 * Contiene información sobre el stock disponible y la sucursal a la que pertenece.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "products")
public class ProductEntity extends BaseNameEntity implements Serializable {

    private Integer stock; // Cantidad en inventario

    @Column("branch_id")
    private Long branchId; // ID de la sucursal a la que pertenece el producto

}

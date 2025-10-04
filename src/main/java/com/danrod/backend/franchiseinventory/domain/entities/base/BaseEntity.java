package com.danrod.backend.franchiseinventory.domain.entities.base;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.util.UUID;

/**
 * Clase base para todas las entidades que incluye un ID y un UUID.
 * Hereda de AuditableEntity para incluir campos de auditoría.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity extends AuditableEntity implements Serializable {

    @Id
    private Long id;

    @Column
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

}

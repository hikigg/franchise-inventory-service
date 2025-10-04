package com.danrod.backend.franchiseinventory.domain.entities.base;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Clase base para entidades que requieren auditoría de creación y modificación.
 * Incluye campos para las marcas de tiempo de creación, actualización y eliminación lógica,
 * así como un indicador de si la entidad está activa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditableEntity implements Serializable {

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @Column("is_active")
    private boolean isActive = true;

    public void changeActive(boolean isActive) {
        if (isActive) {
            this.deletedAt = null;
        } else {
            this.deletedAt = LocalDateTime.now();
        }
        this.isActive = isActive;
    }

}

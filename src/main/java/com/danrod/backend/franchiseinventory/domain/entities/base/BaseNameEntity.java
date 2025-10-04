package com.danrod.backend.franchiseinventory.domain.entities.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * Clase base para entidades que tienen un campo 'name'.
 * Debido a que el proyecto actual cuenta con varias entidades que utilizan un campo 'name',
 * se crea esta clase base para evitar la duplicación de código.
 * Esta clase puede ser extendida por cualquier entidad que requiera un campo 'name'.
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseNameEntity extends BaseEntity implements Serializable {
    private String name;
}

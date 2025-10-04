package com.danrod.backend.franchiseinventory.infrastructure.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String name, String value) {
        super("Ya existe un registro de " + name + " con el valor '" + value + "'");
    }
}
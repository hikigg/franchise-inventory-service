package com.danrod.backend.franchiseinventory.infrastructure.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String name, String value) {
        super("No se encontró el registro de " + name + " con valor " + value);
    }

}
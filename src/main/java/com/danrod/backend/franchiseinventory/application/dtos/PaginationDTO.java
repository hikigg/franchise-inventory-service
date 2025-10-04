package com.danrod.backend.franchiseinventory.application.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para manejar la paginación de resultados.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginationDTO {
    private int currentPage;
    private int totalPages;
    private Long totalItems;

}
package com.alex.eshop.dto.categoryDTOs;

public record CategoryUpdateDTO(
        Long id,
        String name,
        String description
) {
}
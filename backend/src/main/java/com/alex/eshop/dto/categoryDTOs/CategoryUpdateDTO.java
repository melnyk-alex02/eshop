package com.alex.eshop.dto.categoryDTOs;

public record CategoryUpdateDTO(
        Long id,
        String name,
        String description
) {

    public CategoryUpdateDTO withId(Long id) {
        return new CategoryUpdateDTO(id, name, description);
    }

    public CategoryUpdateDTO withName(String name) {
        return new CategoryUpdateDTO(id, name, description);
    }

    public CategoryUpdateDTO withDescription(String description) {
        return new CategoryUpdateDTO(id, name, description);
    }
}
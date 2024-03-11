package com.alex.eshop.dto.categoryDTOs;

public record CategoryDTO(
        Long id,
        String name,
        String description
) {

    public CategoryDTO withId(Long id) {
        return new CategoryDTO(id, name, description);
    }

    public CategoryDTO withName(String name) {
        return new CategoryDTO(id, name, description);
    }

    public CategoryDTO withDescription(String description) {
        return new CategoryDTO(id, name, description);
    }
}

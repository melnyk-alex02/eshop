package com.alex.eshop.dto.categoryDTOs;

public record CategoryCreateDTO(
        String name,

        String description
) {
    public CategoryCreateDTO withName(String name) {
        return new CategoryCreateDTO(name, description);
    }

    public CategoryCreateDTO withDescription(String description) {
        return new CategoryCreateDTO(name, description);
    }
}

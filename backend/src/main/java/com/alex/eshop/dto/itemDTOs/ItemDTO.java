package com.alex.eshop.dto.itemDTOs;

import java.math.BigDecimal;

public record ItemDTO(
        Long id,
        String name,
        Long categoryId,
        String categoryName,
        String description,
        BigDecimal price,
        String imageSrc
) {
    public ItemDTO withId(Long id) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }

    public ItemDTO withName(String name) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }

    public ItemDTO withCategoryId(Long categoryId) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }

    public ItemDTO withCategoryName(String categoryName) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }

    public ItemDTO withDescription(String description) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }

    public ItemDTO withPrice(BigDecimal price) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }

    public ItemDTO withImageSrc(String imageSrc) {
        return new ItemDTO(id, name, categoryId, categoryName, description, price, imageSrc);
    }
}
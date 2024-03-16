package com.alex.eshop.dto.itemDTOs;

import java.math.BigDecimal;

public record ItemCreateDTO(
        String name,
        String description,
        String imageSrc,
        BigDecimal price,
        Long categoryId

) {
    public ItemCreateDTO withName(String name) {
        return new ItemCreateDTO(name, description, imageSrc, price, categoryId);
    }

    public ItemCreateDTO withDescription(String description) {
        return new ItemCreateDTO(name, description, imageSrc, price, categoryId);
    }

    public ItemCreateDTO withImageSrc(String imageSrc) {
        return new ItemCreateDTO(name, description, imageSrc, price, categoryId);
    }

    public ItemCreateDTO withPrice(BigDecimal price) {
        return new ItemCreateDTO(name, description, imageSrc, price, categoryId);
    }

    public ItemCreateDTO withCategoryId(Long categoryId) {
        return new ItemCreateDTO(name, description, imageSrc, price, categoryId);
    }
}
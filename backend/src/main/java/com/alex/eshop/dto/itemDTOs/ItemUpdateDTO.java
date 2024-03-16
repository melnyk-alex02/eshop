package com.alex.eshop.dto.itemDTOs;

import java.math.BigDecimal;

public record ItemUpdateDTO(
        Long id,
        String name,
        String description,
        String imageSrc,
        BigDecimal price,
        Long categoryId

) {
    public ItemUpdateDTO withId(Long id) {
        return new ItemUpdateDTO(id, name, description, imageSrc, price, categoryId);
    }

    public ItemUpdateDTO withName(String name) {
        return new ItemUpdateDTO(id, name, description, imageSrc, price, categoryId);
    }

    public ItemUpdateDTO withDescription(String description) {
        return new ItemUpdateDTO(id, name, description, imageSrc, price, categoryId);
    }

    public ItemUpdateDTO withImageSrc(String imageSrc) {
        return new ItemUpdateDTO(id, name, description, imageSrc, price, categoryId);
    }

    public ItemUpdateDTO withPrice(BigDecimal price) {
        return new ItemUpdateDTO(id, name, description, imageSrc, price, categoryId);
    }

    public ItemUpdateDTO withCategoryId(Long categoryId) {
        return new ItemUpdateDTO(id, name, description, imageSrc, price, categoryId);
    }
}
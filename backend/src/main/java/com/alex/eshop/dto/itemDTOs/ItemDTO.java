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
}
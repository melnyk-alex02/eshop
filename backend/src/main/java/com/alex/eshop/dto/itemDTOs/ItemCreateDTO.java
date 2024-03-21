package com.alex.eshop.dto.itemDTOs;

import java.math.BigDecimal;

public record ItemCreateDTO(
        String name,
        String description,
        String imageSrc,
        BigDecimal price,
        Long categoryId

) {
}
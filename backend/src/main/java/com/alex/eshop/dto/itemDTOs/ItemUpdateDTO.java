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
}
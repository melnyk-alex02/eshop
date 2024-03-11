package com.alex.eshop.dto.statsDTOs;

public record StatsDTO(
        String category,
        long itemsCount
) {
    public StatsDTO withCategory(String category) {
        return new StatsDTO(category, itemsCount);
    }

    public StatsDTO withItemsCount(long itemsCount) {
        return new StatsDTO(category, itemsCount);
    }
}
package com.alex.eshop.dto.statsDTOs;

public class StatsDTO {
     private String category;
     private long itemsCount;

    public StatsDTO(String category, long itemsCount){
        this.category = category;
        this.itemsCount = itemsCount;
    }
    public StatsDTO(){}

    public String getCategory() {
        return category;
    }

    public long getItemsCount() {
        return itemsCount;
    }
}

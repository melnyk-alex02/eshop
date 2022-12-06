package com.alex.eshop.dto;

public class StatsDTO {
     String category;
     long itemsCount;

    public StatsDTO(String category, long itemsCount){
        this.category = category;
        this.itemsCount = itemsCount;
    }
    public StatsDTO(){}
}

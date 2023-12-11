package com.alex.eshop.dto.orderDTOs;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderDTO {
    private String number;
    private String status;
    private ZonedDateTime createdDate;
    private BigDecimal price;
    private Integer count;
    private List<OrderItemDTO> orderItemDTOList;
    private String userId;
    private ZonedDateTime purchasedDate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(ZonedDateTime purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<OrderItemDTO> getOrderItemDTOList() {
        return orderItemDTOList;
    }

    public void setOrderItemDTOList(List<OrderItemDTO> orderItemDTOList) {
        this.orderItemDTOList = orderItemDTOList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
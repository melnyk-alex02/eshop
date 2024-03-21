package com.alex.eshop.entity;

import com.alex.eshop.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"orderItemList"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String number;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private ZonedDateTime createdDate;
    private BigDecimal price;
    private Integer count;
    private String userId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList;
    private ZonedDateTime purchasedDate;
}
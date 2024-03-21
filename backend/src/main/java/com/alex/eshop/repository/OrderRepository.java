package com.alex.eshop.repository;

import com.alex.eshop.constants.OrderStatus;
import com.alex.eshop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"orderItemList", "orderItemList.item"})
    Order findOrderByUserIdAndNumber(String userId, String number);

    boolean existsByNumberAndUserId(String number, String userId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"orderItemList", "orderItemList.item"})
    Order getReferenceByNumber(String number);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"orderItemList", "orderItemList.item"})
    Page<Order> findAllByUserId(String userId, Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"orderItemList", "orderItemList.item"})
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"orderItemList", "orderItemList.item"})
    List<Order> findByStatusAndCreatedDateBefore(OrderStatus status, ZonedDateTime createdDate);
}
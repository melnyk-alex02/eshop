package com.alex.eshop.repository;

import com.alex.eshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    ;

    Order findOrderByUserIdAndNumber(String userId, String number);

    boolean existsByNumberAndUserId(String number, String userId);
}
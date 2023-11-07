package com.alex.eshop.repository;

import com.alex.eshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(String userId);
    List<Cart> findAllByUserId(String userId);

    Cart findCartByItemIdAndUserId(Long itemId, String userId);

    void deleteCartByItemId(Long itemId);

    boolean existsByItemIdAndUserId(Long itemId, String userId);

    boolean existsAllByUserId(String userId);

    void deleteAllByUserId(String userId);
}
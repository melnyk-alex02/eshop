package com.alex.eshop.repository;

import com.alex.eshop.entity.CartItem;
import com.alex.eshop.entity.compositeIds.CartItemId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    @EntityGraph(attributePaths = {"item", "cartItemId"})
    CartItem findByCartItemId(CartItemId cartItemId);

    @EntityGraph(attributePaths = {"item", "cartItemId"})
    List<CartItem> findAllByUserId(String userId);

    void deleteCartByItemId(Long itemId);

    boolean existsByCartItemId(CartItemId cartItemId);

    @EntityGraph(attributePaths = {"item", "cartItemId"})
    boolean existsAllByUserId(String userId);

    @EntityGraph(attributePaths = {"item", "cartItemId"})
    void deleteAllByUserId(String userId);
}
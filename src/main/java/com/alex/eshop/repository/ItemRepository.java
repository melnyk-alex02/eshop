package com.alex.eshop.repository;

import com.alex.eshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

}

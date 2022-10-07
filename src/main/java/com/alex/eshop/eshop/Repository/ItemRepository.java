package com.alex.eshop.eshop.Repository;

import com.alex.eshop.eshop.Entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i")
    List<Item> getLastFiveItems(Pageable pageable);
}

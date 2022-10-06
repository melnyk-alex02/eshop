package com.alex.eshop.eshop.Repository;

import com.alex.eshop.eshop.Entity.Item;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("select i from Item i order by i.id desc")
    List<Item> getLastFiveItems();
    Optional<Item> findById(Long id);
}

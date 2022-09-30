package com.alex.eshop.eshop.Repository;


import com.alex.eshop.eshop.Entity.Category;
import com.alex.eshop.eshop.Entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Component
@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(value = "SELECT id,name,category_id,description,image_src FROM item order by id desc limit 5", nativeQuery = true)
    List<Item> lastFiveItems() ;

    Optional<Item> findById(long id);
}

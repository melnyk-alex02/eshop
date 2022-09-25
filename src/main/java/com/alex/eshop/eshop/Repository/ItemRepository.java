package com.alex.eshop.eshop.Repository;


import com.alex.eshop.eshop.Entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
}

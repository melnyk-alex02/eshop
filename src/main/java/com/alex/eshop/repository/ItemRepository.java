package com.alex.eshop.repository;

import com.alex.eshop.dto.StatsDTO;
import com.alex.eshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
   Page<Item> findByCategoryId(Long categoryId, Pageable pageable);
   boolean existsByCategoryId(Long categoryId);
   @Query("select new com.alex.eshop.dto.StatsDTO(i.category.name, count(i)) from Item i group by i.category.id")
   List<StatsDTO> getStats();
}

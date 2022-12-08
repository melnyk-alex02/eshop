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

    @Query("select new com.alex.eshop.dto.StatsDTO(c.name, count(i.id)) from Category c left join Item i " +
            "on c.id = i.category.id group by c.id")
    List<StatsDTO> getStats();
}

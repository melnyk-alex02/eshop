package com.alex.eshop.repository;

import com.alex.eshop.dto.StatsDTO;
import com.alex.eshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select count(i) from Item i where i.category.id =:id")
    long countItemsByCategory(Long id);

    @Query("select new com.alex.eshop.dto.StatsDTO(c.name, count(i.id)) from Category c left join Item i " +
            "on c.id = i.category.id group by c.id")
    List<StatsDTO> getStats();
}
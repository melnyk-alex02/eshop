package com.alex.eshop.repository;

import com.alex.eshop.dto.statsDTOs.StatsDTO;
import com.alex.eshop.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    @Query("select count(i) from Item i where i.category.id =:id")
    long countItemsByCategory(Long id);

    @Query("select new com.alex.eshop.dto.statsDTOs.StatsDTO(c.name, count(i.id)) from Category c left join Item i " +
            "on c.id = i.category.id group by c.id order by count(i.id) desc")
    Page<StatsDTO> getStats(Pageable pageable);
}
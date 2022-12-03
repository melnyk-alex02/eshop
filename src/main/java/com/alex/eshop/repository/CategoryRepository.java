package com.alex.eshop.repository;

import com.alex.eshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select count(i) from Item i where i.category.id =:id")
    long countItemsByCategory(Long id);
}

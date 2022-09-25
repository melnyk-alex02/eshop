package com.alex.eshop.eshop.Repository;

import com.alex.eshop.eshop.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
}
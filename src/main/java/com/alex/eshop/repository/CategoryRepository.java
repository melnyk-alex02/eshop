package com.alex.eshop.repository;

import com.alex.eshop.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
    Page<Category> findAll(Pageable pageable);
}

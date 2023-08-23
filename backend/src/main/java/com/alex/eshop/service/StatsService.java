package com.alex.eshop.service;

import com.alex.eshop.dto.statsDTOs.StatsDTO;
import com.alex.eshop.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StatsService {
    private final CategoryRepository categoryRepository;

    public StatsService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<StatsDTO> getStats(Pageable pageable) {
        return categoryRepository.getStats(pageable);
    }
}
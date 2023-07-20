package com.alex.eshop.service;

import com.alex.eshop.dto.statsDTOs.StatsDTO;
import com.alex.eshop.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatsService {
    private final CategoryRepository categoryRepository;

    public StatsService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<StatsDTO> getStats() {
        return categoryRepository.getStats();
    }
}
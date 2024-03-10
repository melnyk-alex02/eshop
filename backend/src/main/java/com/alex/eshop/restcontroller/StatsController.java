package com.alex.eshop.restcontroller;

import com.alex.eshop.constants.Role;
import com.alex.eshop.dto.statsDTOs.StatsDTO;
import com.alex.eshop.service.StatsService;
import com.alex.eshop.wrapper.PageWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PreAuthorize("hasRole('" + Role.ADMIN + "')")
    @GetMapping("/stats")
    public PageWrapper<StatsDTO> getStats(Pageable pageable) {
        Page<StatsDTO> statsPage = statsService.getStats(pageable);

        return new PageWrapper<>(statsPage.getContent(), statsPage.getTotalElements());
    }
}
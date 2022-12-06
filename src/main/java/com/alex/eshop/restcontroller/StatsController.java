package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.StatsDTO;
import com.alex.eshop.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {
    private final StatsService statsService;
    public StatsController(StatsService statsService){
        this.statsService = statsService;
    }
    @GetMapping("/stats")
    public List<StatsDTO> getStats(){
        return statsService.getStats();
    }
}

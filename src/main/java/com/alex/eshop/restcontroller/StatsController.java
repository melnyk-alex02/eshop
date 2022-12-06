package com.alex.eshop.restcontroller;

import com.alex.eshop.dto.StatsDTO;
import com.alex.eshop.repository.StatsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatsController {
    private final StatsRepository statsRepository;
    public StatsController(StatsRepository statsRepository){
        this.statsRepository = statsRepository;
    }
    @GetMapping("/stats")
    public List<StatsDTO> getStats(){
        return statsRepository.getAllByCategory();
    }
}

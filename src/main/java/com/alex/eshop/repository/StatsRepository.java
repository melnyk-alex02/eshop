package com.alex.eshop.repository;

import com.alex.eshop.dto.StatsDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface StatsRepository{
    @Query("select new com.alex.eshop.dto.StatsDTO(i.category.name, count(i)) from Item i group by i.category.id")
    List<StatsDTO> getAllByCategory();
}

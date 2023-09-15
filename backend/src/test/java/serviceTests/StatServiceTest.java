package serviceTests;


import com.alex.eshop.dto.statsDTOs.StatsDTO;
import com.alex.eshop.repository.CategoryRepository;
import com.alex.eshop.service.StatsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    public void testGetStats() {
        StatsDTO statsDTO = new StatsDTO();
        statsDTO.setItemsCount(5L);
        statsDTO.setCategory("category");

        Pageable pageable = PageRequest.of(0, 5);

        Page<StatsDTO> statsDTOPage = new PageImpl<>(List.of(statsDTO));

        when(categoryRepository.getStats(pageable)).thenReturn(statsDTOPage);

        Page<StatsDTO> result = statsService.getStats(pageable);

        verify(categoryRepository).getStats(pageable);

        assertEquals(statsDTOPage, result);
    }
}

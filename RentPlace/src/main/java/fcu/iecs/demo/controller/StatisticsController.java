package fcu.iecs.demo.controller;

import fcu.iecs.demo.dto.StatisticsDto.MonthlyStatsDTO;
import fcu.iecs.demo.dto.StatisticsDto.PaymentStatsDTO;
import fcu.iecs.demo.dto.StatisticsDto.VenueStatsDTO;
import fcu.iecs.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

  private final StatisticsService statisticsService;

  @GetMapping("/monthly")
  public List<MonthlyStatsDTO> getMonthlyStats() {
    return statisticsService.getMonthlyStats();
  }

  @GetMapping("/payment")
  public List<PaymentStatsDTO> getPaymentStats() {
    return statisticsService.getPaymentStats();
  }

  @GetMapping("/venue")
  public List<VenueStatsDTO> getVenueStats() {
    return statisticsService.getVenueStats();
  }
}

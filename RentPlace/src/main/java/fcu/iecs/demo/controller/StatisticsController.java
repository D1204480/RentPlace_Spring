package fcu.iecs.demo.controller;

import fcu.iecs.demo.dto.StatisticsDto.MonthlyStatsDTO;
import fcu.iecs.demo.dto.StatisticsDto.PaymentStatsDTO;
import fcu.iecs.demo.dto.StatisticsDto.VenueStatsDTO;
import fcu.iecs.demo.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = {"http://localhost:5174"})
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

  private final StatisticsService statisticsService;

  // 按月份統計收入與預約數
  @GetMapping("/monthly")
  public List<MonthlyStatsDTO> getMonthlyStats() {
    return statisticsService.getMonthlyStats();
  }

  // 支付方式統計
  @GetMapping("/payment")
  public List<PaymentStatsDTO> getPaymentStats() {
    return statisticsService.getPaymentStats();
  }

  // 場地使用率排名
  @GetMapping("/venue")
  public List<VenueStatsDTO> getVenueStats() {
    return statisticsService.getVenueStats();
  }
}

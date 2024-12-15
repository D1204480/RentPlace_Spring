package fcu.iecs.demo.service;

import fcu.iecs.demo.dto.StatisticsDto.MonthlyStatsDTO;
import fcu.iecs.demo.dto.StatisticsDto.PaymentStatsDTO;
import fcu.iecs.demo.dto.StatisticsDto.VenueStatsDTO;
import fcu.iecs.demo.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

  private final StatisticsRepository statisticsRepository;

  public List<MonthlyStatsDTO> getMonthlyStats() {
    return statisticsRepository.getMonthlyStats().stream()
        .map(row -> {
          MonthlyStatsDTO dto = new MonthlyStatsDTO();
          dto.setMonth((String) row[0]);
          dto.setTotalReservations(((Number) row[1]).longValue());
          dto.setTotalRevenue(((Number) row[2]).doubleValue());
          return dto;
        })
        .collect(Collectors.toList());
  }

  public List<PaymentStatsDTO> getPaymentStats() {
    return statisticsRepository.getPaymentStats().stream()
        .map(row -> {
          PaymentStatsDTO dto = new PaymentStatsDTO();
          dto.setPaymentMethod((String) row[0]);
          dto.setCount(((Number) row[1]).longValue());
          dto.setPercentage(((Number) row[2]).doubleValue());
          return dto;
        })
        .collect(Collectors.toList());
  }

  public List<VenueStatsDTO> getVenueStats() {
    return statisticsRepository.getVenueStats().stream()
        .map(row -> {
          VenueStatsDTO dto = new VenueStatsDTO();
          dto.setVenueId(((Number) row[0]).intValue());
          dto.setVenueName(((String) row[1]));
          dto.setReservedTimes(((Number) row[2]).longValue());
          dto.setUsageRate(((Number) row[3]).doubleValue());
          return dto;
        })
        .collect(Collectors.toList());
  }
}
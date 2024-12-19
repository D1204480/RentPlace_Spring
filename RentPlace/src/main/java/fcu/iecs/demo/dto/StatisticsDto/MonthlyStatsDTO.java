package fcu.iecs.demo.dto.StatisticsDto;

import lombok.Data;

@Data
public class MonthlyStatsDTO {
    private String month;
    private Long totalReservations;
    private Double totalRevenue;
}
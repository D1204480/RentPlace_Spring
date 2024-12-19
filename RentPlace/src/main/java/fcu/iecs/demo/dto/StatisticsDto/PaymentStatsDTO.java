package fcu.iecs.demo.dto.StatisticsDto;

import lombok.Data;

@Data
public class PaymentStatsDTO {
    private String paymentMethod;
    private Long count;
    private Double percentage;
}
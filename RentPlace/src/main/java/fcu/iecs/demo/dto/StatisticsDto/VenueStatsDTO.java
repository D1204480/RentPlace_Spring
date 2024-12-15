package fcu.iecs.demo.dto.StatisticsDto;

import lombok.Data;

@Data
public class VenueStatsDTO {
    private Long venueId;
    private Long reservedTimes;
    private Double usageRate;
}
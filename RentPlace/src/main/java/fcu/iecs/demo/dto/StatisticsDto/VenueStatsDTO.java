package fcu.iecs.demo.dto.StatisticsDto;

import lombok.Data;

@Data
public class VenueStatsDTO {
    private Integer venueId;
    private String venueName;
    private Long reservedTimes;
    private Double usageRate;
}
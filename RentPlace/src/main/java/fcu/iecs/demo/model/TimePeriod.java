package fcu.iecs.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Time_Period")
public class TimePeriod {
  @Id
  @Column(name = "time_period_id")
  private Integer timePeriodId;

  @Column(name = "time_period")
  private String timePeriod;

  // getters and setters
  public Integer getTimePeriodId() {
    return timePeriodId;
  }

  public void setTimePeriodId(Integer timePeriodId) {
    this.timePeriodId = timePeriodId;
  }

  public String getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(String timePeriod) {
    this.timePeriod = timePeriod;
  }
}
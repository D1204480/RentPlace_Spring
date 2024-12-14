package fcu.iecs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation_TimePeriod")
public class ReservationTimePeriod {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_timeperiod_id")
  private Integer reservationTimeperiodId;

  @Column(name = "reservation_id")
  private Integer reservationId;

  @Column(name = "time_period_id")
  private Integer timePeriodId;

  // getters and setters


  public Integer getReservationTimeperiodId() {
    return reservationTimeperiodId;
  }

  public void setReservationTimeperiodId(Integer reservationTimeperiodId) {
    this.reservationTimeperiodId = reservationTimeperiodId;
  }

  public Integer getReservationId() {
    return reservationId;
  }

  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

  public Integer getTimePeriodId() {
    return timePeriodId;
  }

  public void setTimePeriodId(Integer timePeriodId) {
    this.timePeriodId = timePeriodId;
  }
}

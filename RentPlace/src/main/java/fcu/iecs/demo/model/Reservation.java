package fcu.iecs.demo.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "venue_id", nullable = false)
    private Integer venueId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "time_period_id", nullable = false)
    private Integer timePeriodId;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "remark")
    private String remark;

    @Column(name = "apply_apartment")
    private String applyApartment;

    @Column(name = "content")
    private String content;

    // Getters and Setters
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTimePeriodId() {
        return timePeriodId;
    }

    public void setTimePeriodId(Integer timePeriodId) {
        this.timePeriodId = timePeriodId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplyApartment() {
        return applyApartment;
    }

    public void setApplyApartment(String applyApartment) {
        this.applyApartment = applyApartment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

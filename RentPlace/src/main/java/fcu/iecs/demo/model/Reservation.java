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

    // 標住但不建立關聯
    @Column(name = "venue_id", insertable = false, updatable = false)
    private Integer venueId;

    @Column(name = "user_id", nullable = false)
    private String userId;

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

    @Column(name = "status_id")
    private Integer statusId;

    @ManyToOne
    @JoinColumn(name = "venue_id")  // 這裡指定資料庫中的外鍵欄位名稱
    private Venue venue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_period_id", referencedColumnName = "time_period_id", insertable = false, updatable = false)
    private TimePeriod timePeriod;

    // 與 Status 的關聯表
    @ManyToOne(fetch = FetchType.EAGER)  // 確保在查詢 Reservation 時會自動載入關聯的 Status 資訊
    @JoinColumn(name = "status_id", referencedColumnName = "status_id", insertable=false, updatable=false)   // 明確指定關聯欄位
    private Status statusInfo;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Status getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(Status statusInfo) {
        this.statusInfo = statusInfo;
    }

}

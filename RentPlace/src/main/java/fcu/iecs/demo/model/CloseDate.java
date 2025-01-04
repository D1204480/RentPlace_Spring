package fcu.iecs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Close_Date")
public class CloseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "close_date_id")
    private Integer closeDateId;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Column(name = "venue_id")
    private Integer venueId;

    @Column(name = "status_id")
    private Integer statusId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venue_id", referencedColumnName = "venue_id", insertable = false, updatable = false)
    private Venue venue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "status_id", insertable = false, updatable = false)
    private Status statusInfo;

    // getters and setters
    public Integer getCloseDateId() {
        return closeDateId;
    }

    public void setCloseDateId(Integer closeDateId) {
        this.closeDateId = closeDateId;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
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

    public Status getStatus() {
        return statusInfo;
    }

    public void setStatus(Status status) {
        this.statusInfo = statusInfo;
    }
}
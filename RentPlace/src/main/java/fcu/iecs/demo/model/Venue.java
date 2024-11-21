package fcu.iecs.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Venue")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id")
    private Integer id;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "venue_type", nullable = false)
    private String venueType;

    @Column(name = "venue_name", nullable = false)
    private String venueName;

    @Column(name = "region_name", nullable = false)
    private String regionName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "unit_type", nullable = false)
    private String unitType;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "available_time", nullable = false)
    private String availableTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "phone_number")
    private String phoneNumber;
}

package fcu.iecs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

//    @Column(name = "unit_type", nullable = false)
//    private String unitType;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "available_time", nullable = false)
    private String availableTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "image_id")
    private Integer imageId;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "image_id", insertable = false, updatable = false)
    private Image imageName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties("venue")
    private List<Equipment> equipment;

    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties({"venue"})
    private List<CloseDate> closeDates;


    // 新增 getter
    public String getImageName() {
        return imageName != null ? imageName.getImageName() : null;
    }
}

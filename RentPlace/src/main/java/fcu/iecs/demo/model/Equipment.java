package fcu.iecs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer id;

    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnoreProperties("equipment")
    private Venue venue;
}

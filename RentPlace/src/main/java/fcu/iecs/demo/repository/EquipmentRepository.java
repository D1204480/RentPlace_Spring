package fcu.iecs.demo.repository;


import fcu.iecs.demo.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    // You can define custom query methods here if needed
    @Query("SELECT e FROM Equipment e JOIN FETCH e.venue v WHERE v.id = :venueId")
    List<Equipment> findAllByVenueId(@Param("venueId") Integer venueId);
}


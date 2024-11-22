package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.CloseDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CloseDateRepository extends JpaRepository<CloseDate, Integer> {
    @Query("SELECT cd FROM CloseDate cd JOIN FETCH cd.venue JOIN FETCH cd.status")
    List<CloseDate> findAllWithVenueAndStatus();
}
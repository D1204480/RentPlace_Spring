package fcu.iecs.demo.repository;



import fcu.iecs.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r JOIN FETCH r.timePeriod_statusInfo")
    List<Reservation> findAllWithStatus();

    @Query("SELECT r FROM Reservation r JOIN FETCH r.timePeriod JOIN FETCH r.timePeriod_statusInfo")
    List<Reservation> findAllWithTimePeriodAndStatus();

    @Query("SELECT r FROM Reservation r JOIN FETCH r.timePeriod JOIN FETCH r.timePeriod_statusInfo WHERE r.reservationId = :id")
    Optional<Reservation> findByIdWithTimePeriodAndStatus(@Param("id") Integer id);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.venue JOIN FETCH r.timePeriod_statusInfo WHERE r.venue.id = :venueId")
    List<Reservation> findAllByVenueIdWithVenueAndStatus(@Param("venueId") Integer venueId);

    @Query("SELECT DISTINCT r FROM Reservation r LEFT JOIN FETCH r.reservationEquipments re LEFT JOIN FETCH re.equipment")
    List<Reservation> findAllWithEquipments();
}

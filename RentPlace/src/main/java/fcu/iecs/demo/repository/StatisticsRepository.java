package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Reservation, Integer> {

  @Query(value =
      "SELECT DATE_FORMAT(reservation_date, '%Y-%m') AS month, " +
          "COUNT(*) AS total_reservations, " +
          "SUM(o.total_amount) AS total_revenue " +
          "FROM Reservations r, Orders o " +
          "WHERE o.status_id = 5 " +
          "GROUP BY month ORDER BY month", nativeQuery = true)
  List<Object[]> getMonthlyStats();

  @Query(value =
      "SELECT payment_method, COUNT(*) AS count, " +
          "ROUND((COUNT(*) / (SELECT COUNT(*) FROM Orders)) * 100, 2) AS percentage " +
          "FROM Orders o, Payment p " +
          "GROUP BY payment_method", nativeQuery = true)
  List<Object[]> getPaymentStats();

  @Query(value =
      "SELECT v.venue_id, v.venue_name, COUNT(*) AS reserved_times, " +
          "(COUNT(*) / (SELECT COUNT(*) FROM Reservations r2)) * 100 AS usage_rate " +
          "FROM Reservations r, Venue v " +
          "WHERE r.status_id = 4 " +
          "GROUP BY v.venue_id", nativeQuery = true)
  List<Object[]> getVenueStats();
}

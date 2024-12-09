package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
  List<Order> findByUserId(String userId);
  List<Order> findByStatusId(Integer statusId);
  Optional<Order> findByReservationId(Integer reservationId);
}
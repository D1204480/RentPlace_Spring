package fcu.iecs.demo.repository;

import fcu.iecs.demo.model.Payment;
import fcu.iecs.demo.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
  List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
  List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
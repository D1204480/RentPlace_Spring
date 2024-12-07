package fcu.iecs.demo.service;

import fcu.iecs.demo.model.Payment;
import fcu.iecs.demo.model.PaymentMethod;
import fcu.iecs.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
  @Autowired
  private PaymentRepository paymentRepository;

  public Payment createPayment(Payment payment) {
    payment.setPaymentDate(LocalDateTime.now());
    return paymentRepository.save(payment);
  }

  public Payment getPaymentById(Integer paymentId) {
    return paymentRepository.findById(paymentId)
        .orElseThrow(() -> new RuntimeException("Payment not found"));
  }

  public List<Payment> getAllPayments() {
    return paymentRepository.findAll();
  }

  public List<Payment> getPaymentsByMethod(PaymentMethod paymentMethod) {
    return paymentRepository.findByPaymentMethod(paymentMethod);
  }

  public Payment updatePayment(Integer paymentId, Payment paymentDetails) {
    Payment payment = getPaymentById(paymentId);
    payment.setPaymentMethod(paymentDetails.getPaymentMethod());
    return paymentRepository.save(payment);
  }

  public void deletePayment(Integer paymentId) {
    Payment payment = getPaymentById(paymentId);
    paymentRepository.delete(payment);
  }
}
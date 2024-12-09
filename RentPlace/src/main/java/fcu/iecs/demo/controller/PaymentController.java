package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.Payment;
import fcu.iecs.demo.model.PaymentMethod;
import fcu.iecs.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
  @Autowired
  private PaymentService paymentService;

  @PostMapping
  public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
    Payment savedPayment = paymentService.createPayment(payment);
    return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Payment> getPayment(@PathVariable Integer id) {
    Payment payment = paymentService.getPaymentById(id);
    return ResponseEntity.ok(payment);
  }

  @GetMapping
  public ResponseEntity<List<Payment>> getAllPayments() {
    List<Payment> payments = paymentService.getAllPayments();
    return ResponseEntity.ok(payments);
  }

  @GetMapping("/method/{paymentMethod}")
  public ResponseEntity<List<Payment>> getPaymentsByMethod(
      @PathVariable PaymentMethod paymentMethod) {
    List<Payment> payments = paymentService.getPaymentsByMethod(paymentMethod);
    return ResponseEntity.ok(payments);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Payment> updatePayment(
      @PathVariable Integer id,
      @RequestBody Payment payment) {
    Payment updatedPayment = paymentService.updatePayment(id, payment);
    return ResponseEntity.ok(updatedPayment);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePayment(@PathVariable Integer id) {
    paymentService.deletePayment(id);
    return ResponseEntity.ok().build();
  }
}
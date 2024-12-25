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
  public String generateVirtualAccount() {
    // 生成隨機虛擬帳號，例如 12 位數字
    String virtualAccount = String.valueOf((long) (Math.random() * 1_000_000_000_000L));

    // 將虛擬帳號儲存到資料庫
    Payment payment = new Payment();
    payment.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
    payment.setPaymentDate(LocalDateTime.now());
    payment.setVirtualAccount(virtualAccount); // 確保此處正確設置值
    // 如果需要，將虛擬帳號與用戶或訂單關聯
    // payment.setUserId(userId); // 假設有 userId
    // payment.setOrderId(orderId); // 假設有 orderId
    paymentRepository.save(payment);
    System.out.println("Generated Virtual Account: " + virtualAccount);
    return virtualAccount;
  }
}
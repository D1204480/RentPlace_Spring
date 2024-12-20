package fcu.iecs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Integer orderId;

  @Column(name = "reservation_id")
  private Integer reservationId;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "payment_id")
  private Integer paymentId;

  @Column(name = "order_date")
  private LocalDate orderDate;

  @Column(name = "status_id")
  private Integer statusId;

  @Column(name = "total_amount")
  private BigDecimal totalAmount;

  // 關聯
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
  private Reservation reservation;

  // 關聯
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "payment_id", insertable = false, updatable = false)
  private Payment payment;

  // 關聯
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status_id", insertable = false, updatable = false)
  private Status status;

  // Getters and Setters
  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public Integer getReservationId() {
    return reservationId;
  }

  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(Integer paymentId) {
    this.paymentId = paymentId;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  public Integer getStatusId() {
    return statusId;
  }

  public void setStatusId(Integer statusId) {
    this.statusId = statusId;
  }

  // 關聯的 Getters and Setters
  public Reservation getReservation() {
    return reservation;
  }

  public Payment getPayment() {
    return payment;
  }

  public Status getStatus() {
    return status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
}

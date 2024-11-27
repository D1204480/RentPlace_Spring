package fcu.iecs.demo.model;


import jakarta.persistence.*;
@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "reservation_id")
    private int reservationId;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;


    @Column(name = "payment_id")
    private int paymentId;

    @Column(name = "order_date")
    private String orderDate; // 使用 String 儲存日期，若使用 LocalDate 則需要進行格式化

    @Column(name = "status_id")
    private int statusId;

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}


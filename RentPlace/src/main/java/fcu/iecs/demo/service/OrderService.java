package fcu.iecs.demo.service;

import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Transactional
  public Order createOrder(Order order) {
    return orderRepository.save(order);
  }

  public Order getOrderById(Integer orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));
  }

  public Order getOrderByReservationId(Integer reservationId) {
    return orderRepository.findByReservationId(reservationId)
        .orElseThrow(() -> new RuntimeException("Order not found"));
  }

  public List<Order> getOrdersByUserId(String userId) {
    return orderRepository.findByUserId(userId);
  }

  public List<Order> getOrdersByStatus(Integer statusId) {
    return orderRepository.findByStatusId(statusId);
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  @Transactional
  public Order updateOrder(Integer orderId, Order orderDetails) {
    Order order = getOrderById(orderId);
    order.setPaymentId(orderDetails.getPaymentId());
    order.setStatusId(orderDetails.getStatusId());
    return orderRepository.save(order);
  }

  @Transactional
  public void deleteOrder(Integer orderId) {
    Order order = getOrderById(orderId);
    orderRepository.delete(order);
  }
}

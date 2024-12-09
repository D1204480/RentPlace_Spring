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

// import java.util.List;
// import java.util.Optional;

// @Service
// public class OrderService {

//     @Autowired
//     private OrderRepository orderRepository;

//     // 查詢所有訂單
//     public List<Order> getAllOrders() {
//         return orderRepository.findAll();
//     }

//     // 根據訂單 ID 查詢訂單
//     public Optional<Order> getOrderById(int orderId) {
//         return orderRepository.findById(orderId);
//     }

//     // 新增訂單
//     public Order createOrder(Order order) {
//         return orderRepository.save(order);
//     }

//     // 更新訂單
//     public Optional<Order> updateOrder(int orderId, Order order) {
//         if (orderRepository.existsById(orderId)) {
//             order.setOrderId(orderId);
//             return Optional.of(orderRepository.save(order));
//         }
//         return Optional.empty();
//     }

//     // 刪除訂單
//     public boolean deleteOrder(int orderId) {
//         if (orderRepository.existsById(orderId)) {
//             orderRepository.deleteById(orderId);
//             return true;
//         }
//         return false;
//     }
 }

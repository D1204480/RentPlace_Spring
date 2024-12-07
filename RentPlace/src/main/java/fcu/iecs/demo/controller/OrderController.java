package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody Order order) {
    order.setOrderDate(LocalDate.now());
    Order savedOrder = orderService.createOrder(order);
    return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable Integer id) {
    Order order = orderService.getOrderById(id);
    return ResponseEntity.ok(order);
  }

  @GetMapping("/reservation/{reservationId}")
  public ResponseEntity<Order> getOrderByReservationId(@PathVariable Integer reservationId) {
    Order order = orderService.getOrderByReservationId(reservationId);
    return ResponseEntity.ok(order);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String userId) {
    List<Order> orders = orderService.getOrdersByUserId(userId);
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/status/{statusId}")
  public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Integer statusId) {
    List<Order> orders = orderService.getOrdersByStatus(statusId);
    return ResponseEntity.ok(orders);
  }

  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    List<Order> orders = orderService.getAllOrders();
    return ResponseEntity.ok(orders);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Order> updateOrder(
      @PathVariable Integer id,
      @RequestBody Order order) {
    Order updatedOrder = orderService.updateOrder(id, order);
    return ResponseEntity.ok(updatedOrder);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
    orderService.deleteOrder(id);
    return ResponseEntity.ok().build();
  }
}
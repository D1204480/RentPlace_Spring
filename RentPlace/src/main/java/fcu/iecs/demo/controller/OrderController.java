package fcu.iecs.demo.controller;

import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import fcu.iecs.demo.service.OrderQRCodeService;
import fcu.iecs.demo.qrcode.QRCodeDecoder;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  @Autowired
    private OrderQRCodeService orderQRCodeService;
    
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

  // 根據訂單 ID 生成 QR Code
  @GetMapping("/{orderId}/qrcode")
  public ResponseEntity<byte[]> generateQRCode(@PathVariable int orderId) {
      try {
          byte[] qrCodeBytes = orderQRCodeService.generateQRCodeForOrder(orderId);
          return ResponseEntity.ok()
                  .header("Content-Type", "image/png")
                  .header("Content-Disposition", "attachment; filename=\"qrcode.png\"")
                  .body(qrCodeBytes);
      } catch (RuntimeException e) {
          return ResponseEntity.status(500).body(null);
      }
  }

    @PostMapping("/qrcode/decode")
    public ResponseEntity<String> decodeQRCode(@RequestBody String base64Image) {
        try {
            String imageData = base64Image.split(",")[1];
            byte[] decodedBytes = Base64.getDecoder().decode(imageData);
            String decodedText = QRCodeDecoder.decodeQRCode(decodedBytes);
            return ResponseEntity.ok(decodedText);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid QR Code");
        }
    }

    @GetMapping("/latest-qrcode")
    public ResponseEntity<byte[]> getLatestQRCode() {
        try {
            byte[] latestQRCodeBytes = orderQRCodeService.getLatestGeneratedQRCode();
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .header("Content-Disposition", "attachment; filename=\"latest-qrcode.png\"")
                    .body(latestQRCodeBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/qrcode/decrypt")
    public ResponseEntity<String> decryptQRCode(@RequestBody String encryptedContent) {
        try {
            String decryptedContent = orderQRCodeService.decryptQRCodeContent(encryptedContent);
            return ResponseEntity.ok(decryptedContent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error decrypting QR Code: " + e.getMessage());
        }
    }
}
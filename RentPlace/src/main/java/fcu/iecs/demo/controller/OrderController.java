package fcu.iecs.demo.controller;

import fcu.iecs.demo.service.OrderQRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fcu.iecs.demo.qrcode.QRCodeDecoder;
import java.util.Base64;
import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.service.OrderService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderQRCodeService orderQRCodeService;
    @Autowired
    private OrderService orderService;
    // 查詢所有訂單
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // 根據訂單 ID 查詢訂單
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 新增訂單
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }

    // 更新訂單
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable int orderId, @RequestBody Order order) {
        Optional<Order> updatedOrder = orderService.updateOrder(orderId, order);
        return updatedOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 刪除訂單
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderId) {
        boolean isDeleted = orderService.deleteOrder(orderId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    // 根據訂單 ID 生成 QR Code
    @GetMapping("/{orderId}/qrcode")
    public ResponseEntity<String> generateQRCode(@PathVariable int orderId) {
        try {
            String qrCodeBase64 = orderQRCodeService.generateQRCodeForOrder(orderId);
            return ResponseEntity.ok(qrCodeBase64);
        } catch (RuntimeException e) {
            // 捕獲到異常，返回 500 Internal Server Error 並顯示錯誤信息
            return ResponseEntity.status(500).body("Error generating QR Code: " + e.getMessage());
        }
    }
    @PostMapping("/qrcode/decode")
    public ResponseEntity<String> decodeQRCode(@RequestBody String base64Image) {
        try {
            // 去掉 Base64 的前綴
            String imageData = base64Image.split(",")[1];
            byte[] decodedBytes = Base64.getDecoder().decode(imageData);
            String decodedText = QRCodeDecoder.decodeQRCode(decodedBytes);
            return ResponseEntity.ok(decodedText);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid QR Code");
        }
    }

    // 查詢最近生成的 QR Code
    @GetMapping("/latest-qrcode")
    public ResponseEntity<String> getLatestQRCode() {
        try {
            String qrCodeBase64 = orderQRCodeService.getLastGeneratedQRCode();
            return ResponseEntity.ok(qrCodeBase64);
        } catch (RuntimeException e) {
            // 捕獲到異常，返回 500 Internal Server Error 並顯示錯誤信息
            return ResponseEntity.status(500).body("Error retrieving QR Code: " + e.getMessage());
        }
    }
}

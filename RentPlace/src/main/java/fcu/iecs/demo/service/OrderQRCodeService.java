package fcu.iecs.demo.service;

import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.repository.OrderRepository;
import fcu.iecs.demo.qrcode.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderQRCodeService {

    @Autowired
    private OrderRepository orderRepository;

    // 用於儲存 QR Code 的變數，這裡我們假設每個訂單會對應一個 QR Code
    private String lastGeneratedQRCode = "";

    // 根據 orderId 生成 QR Code
    public String generateQRCodeForOrder(int orderId) {
        try {
            // 從資料庫查詢訂單資料
            Order order = orderRepository.findByOrderId(orderId);

            if (order == null) {
                throw new RuntimeException("Order not found");
            }

            // 生成 QR Code 並返回 Base64 字串
            return QRCodeGenerator.generateQRCode(order, 300, 300); // 300x300 大小的 QR Code
        } catch (Exception e) {
            // 捕獲並處理異常
            throw new RuntimeException("Error generating QR Code for order " + orderId, e);
        }
    }

    // 提供一個方法來返回當前最新的 QR Code
    public String getLastGeneratedQRCode() {
        return lastGeneratedQRCode;
    }
}

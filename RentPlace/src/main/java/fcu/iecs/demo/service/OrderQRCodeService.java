package fcu.iecs.demo.service;

import fcu.iecs.demo.model.Order;
import fcu.iecs.demo.repository.OrderRepository;
import fcu.iecs.demo.qrcode.QRCodeGenerator;
import fcu.iecs.demo.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderQRCodeService {

    private static final Logger logger = Logger.getLogger(OrderQRCodeService.class.getName());

    @Autowired
    private OrderRepository orderRepository;

    private String secretKey = "1234567890123456"; // 密鑰建議從環境變數獲取
    private byte[] latestGeneratedQRCode;

    public byte[] generateQRCodeForLatestOrder() {
        try {
            // 獲取最新訂單
            Order latestOrder = orderRepository.findTopByOrderByOrderIdDesc();
            if (latestOrder == null) {
                throw new RuntimeException("No order found to generate QR Code.");
            }

            return generateQRCodeForOrder(latestOrder.getOrderId());
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR Code for the latest order.", e);
        }
    }

    public byte[] generateQRCodeForOrder(int orderId) {
        try {
            // 查詢指定 ID 的訂單
            Order order = orderRepository.findByOrderId(orderId);
            if (order == null) {
                throw new RuntimeException("Order not found for ID: " + orderId);
            }

            // 動態因子，例如 UUID 或當前時間戳
            String dynamicFactor = UUID.randomUUID().toString();
            String timestamp = java.time.Instant.now().toString();

            // 原始內容
            String rawContent = String.format(
                    "Order ID: %d\nUser ID: %s\nReservation ID: %d\nOrder Date: %s\nStatus ID: %d\nTimestamp: %s\nDynamic: %s",
                    order.getOrderId(),
                    order.getUserId(),
                    order.getReservationId(),
                    order.getOrderDate(),
                    order.getStatusId(),
                    timestamp,
                    dynamicFactor
            );

            // 加密內容
            String encryptedContent = AESUtil.encrypt(rawContent, secretKey);

            // 生成 QR Code
            latestGeneratedQRCode = QRCodeGenerator.generateQRCodeFromString(encryptedContent, 300, 300);
            return latestGeneratedQRCode;
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR Code for order ID: " + orderId, e);
        }
    }

    public byte[] getLatestGeneratedQRCode() {
        if (latestGeneratedQRCode == null) {
            throw new RuntimeException("No QR Code has been generated yet");
        }
        return latestGeneratedQRCode;
    }

    public String decryptQRCodeContent(String encryptedContent) {
        try {
            return AESUtil.decrypt(encryptedContent, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting QR Code content", e);
        }
    }
}

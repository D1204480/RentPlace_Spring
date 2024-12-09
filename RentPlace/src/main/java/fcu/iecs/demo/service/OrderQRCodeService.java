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
    private String lastGeneratedQRCode; // 用於保存最近生成的 QR Code

    // 生成加密的 QR Code
    public String generateQRCodeForOrder(int orderId) {
        try {
            // 查詢訂單
            logger.info("Fetching order with ID: " + orderId);
            Order order = orderRepository.findByOrderId(orderId);
            if (order == null) {
                logger.warning("Order not found for ID: " + orderId);
                throw new RuntimeException("Order not found for ID: " + orderId);
            }

            // 動態因子，例如 UUID 或時間戳
            String dynamicFactor = UUID.randomUUID().toString();

            // 原始內容
            String rawContent = String.format(
                    "Order ID: %d\nUser ID: %s\nReservation ID: %d\nOrder Date: %s\nStatus ID: %d\nDynamic: %s",
                    order.getOrderId(),
                    order.getUserId(),
                    order.getReservationId(),
                    order.getOrderDate(),
                    order.getStatusId(),
                    dynamicFactor
            );

            // 加密內容
            String encryptedContent = AESUtil.encrypt(rawContent, secretKey);
            logger.info("Encrypted QR Code content: " + encryptedContent);

            // 生成 QR Code
            lastGeneratedQRCode = QRCodeGenerator.generateQRCodeFromString(encryptedContent, 300, 300);
            return lastGeneratedQRCode;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating QR Code for order ID: " + orderId, e);
            throw new RuntimeException("Error generating QR Code for order " + orderId, e);
        }
    }

    // 獲取最近生成的 QR Code
    public String getLastGeneratedQRCode() {
        if (lastGeneratedQRCode == null) {
            throw new RuntimeException("No QR Code has been generated yet");
        }
        return lastGeneratedQRCode;
    }

    // 解密 QR Code 內容
    public String decryptQRCodeContent(String encryptedContent) {
        try {
            return AESUtil.decrypt(encryptedContent, secretKey);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error decrypting QR Code content", e);
            throw new RuntimeException("Error decrypting QR Code content", e);
        }
    }
}

package fcu.iecs.demo.qrcode;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class QRCodeGenerator {

    // 將訂單資料轉換為 JSON 字串並生成 QR Code
    public static String generateQRCode(Object object, int width, int height) throws WriterException {
        // 使用 Gson 將物件轉為 JSON 字串
        Gson gson = new Gson();
        String qrContent = gson.toJson(object);

        // 使用 ZXing 生成 QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

        // 將 QR Code 轉換為 Base64 編碼的 PNG 圖片
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream, new MatrixToImageConfig());
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR Code", e);
        }
    }
}

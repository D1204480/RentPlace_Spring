package fcu.iecs.demo.qrcode;

import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class QRCodeDecoder {

    // 解碼 QR Code 並返回 JSON 字串
    public static String decodeQRCode(byte[] qrCodeImage) throws Exception {
        try {
            // 將圖片轉為 QRCodeReader 可解析的 BinaryBitmap
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrCodeImage);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            QRCodeReader reader = new QRCodeReader();
            return reader.decode(bitmap).getText(); // 返回解碼內容
        } catch (IOException | com.google.zxing.NotFoundException e) {
            throw new RuntimeException("Error decoding QR Code", e);
        }
    }
}

package fcu.iecs.demo.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QRCodeGenerator {

    private static final Logger logger = Logger.getLogger(QRCodeGenerator.class.getName());

    public static String generateQRCodeFromString(String content, int width, int height) throws WriterException {
        try {
            if (content.length() > 2000) {
                throw new IllegalArgumentException("QR Code content is too long to encode. Length: " + content.length());
            }

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream, new MatrixToImageConfig());
                return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating QR Code", e);
            throw new RuntimeException("Error generating QR Code", e);
        }
    }
}

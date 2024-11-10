package poly.foodease.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Component
public class QrCodeUtils {
    public byte[] generateQrCodeText(String text, int width , int height) throws WriterException, IOException {
        // QRCodeWriter : là đối tượng dùng để tạo QR Code của ZXing.
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // BitMatrix : là lớp biểu diễn ma trận 2 chiều của các điểm và ảnh . Ma trận này thể hiện mã QR
        // encode là phương thức chuyển từ text thành đối tượng BitMatrix
        // nó lưu dữ liệu thành dạng bit 1 , 0 (điểm đen và trắng).
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE , width,height);
        // ByteArrayOutputStream : luông ghi dữ liệu thành dạng Byte
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        // Đọc dữ liệu từ bitMatrix và chuyển thành dạng hình ảnh . Sau đó lưu vào pngOutPutStream.
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        // Trả dữ liệu được lưu vào (QR) dưới dạng byte[] : một file dưới dạng API HTTP
        // API HTTP : cho phép gửi dữ liệu dễ dàng thông qua HTTP trong bộ nhớ RAM, không cần tạo file lưu tạm thời.
        return pngOutputStream.toByteArray();
    }

    public File generateQrCodeFile(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE , width, height);
        // Là đối tượng để biểu diễn hình ảnh trong bộ nhớ.
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        // Tempfile : Tạo file tạm thời trong hệ điều hành , tư xóa khi chương trình kết thúc.
        File tempFile = File.createTempFile("qr_", ".png");
        // Ghi đối tượng bufferedImage vào file tạm thời.
        // ImageIO cũng có thể lưu vào hệ thống phục vụ lâu dài.
        ImageIO.write(bufferedImage, "PNG", tempFile);
        // Trả về file đã tạo
        return tempFile;
    }
}
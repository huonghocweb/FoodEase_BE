package poly.foodease.Service;

import com.google.zxing.WriterException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public interface QrCodeService {
    byte[] createQrCode(String text, int width, int height) throws IOException, WriterException;
    File createQrCodeWithFileTemp(String text, int width, int height) throws IOException, WriterException;
}
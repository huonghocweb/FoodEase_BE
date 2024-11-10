package poly.foodease.ServiceImpl;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.foodease.Service.QrCodeService;
import poly.foodease.Utils.QrCodeUtils;

import java.io.File;
import java.io.IOException;

@Service
public class QrCodeServiceImpl implements QrCodeService {

    @Autowired
    QrCodeUtils qrCodeUtils;

    @Override
    public byte[] createQrCode(String text, int width, int height) throws IOException, WriterException {
        return qrCodeUtils.generateQrCodeText(text, width, height);
    }

    @Override
    public File createQrCodeWithFileTemp(String text, int width, int height) throws IOException, WriterException {
        return qrCodeUtils.generateQrCodeFile(text, width, height);
    }


}
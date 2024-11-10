package poly.foodease.Utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Component
public class MailUtiils {
    // Hàm chuyền nội dung của email thành 1 chuỗi String
    public String[] parseStringEmailToArray(String emailString){
        String[] arrEmail = null;
        if(!emailString.isEmpty()){
            emailString = removeSpace(emailString);
            arrEmail  = emailString.split(",");
        }
        return arrEmail ;
    }

    // Loại bỏ khoảng trắng của string bằng cách thay thế bằng khoảng liền .
    private String removeSpace(String string){
        return string.replaceAll(" ", "");
    }

    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Tạo thư mục tạm thời để lưu file,sau này có thể xóa thư mục tạm thời đi
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }
}

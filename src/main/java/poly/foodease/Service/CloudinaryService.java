package poly.foodease.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    // Cấu hình đối tượng cloudinary
    public CloudinaryService(@Value("${cloudinary.cloud_name}") String cloudName,
                             @Value("${cloudinary.api_key}") String apiKey,
                             @Value("${cloudinary.api_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name" ,cloudName,
                "api_key" ,apiKey,
                "api_secret" ,apiSecret
        ));
    }

//    public List<Map<String, Object>> uploadFile(MultipartFile[] files, String folder) throws IOException {
//        List<Map<String, Object>> fileInfos = new ArrayList<>();
//        Map<String, Object> options = ObjectUtils.asMap("folder", folder);
//        for (MultipartFile file : files) {
//            Map<String, Object> fileInfo = cloudinary.uploader().upload(file.getBytes(), options);
//            fileInfos.add(fileInfo);
//            System.out.println(fileInfo);
//        }
//        return fileInfos;
//    }
    public List<String> uploadFile(MultipartFile[] files, String folder) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        Map<String, Object> options = ObjectUtils.asMap("folder", folder);

        for (MultipartFile file : files) {
            Map<String, Object> fileInfo = cloudinary.uploader().upload(file.getBytes(), options);
            // khi lưu image vào cloudinary , nó sẽ trả về 1 Map chứa nhiều thông tin quan trọng như assetId, public_id,width, height,...
            // secure_url là đường dẫn trực tiếp để lấy về hình ảnh đó từ Cloudinary
            String imageUrl = (String) fileInfo.get("secure_url");
            fileUrls.add(imageUrl);
            System.out.println("Uploaded file URL: " + imageUrl);
        }
        return fileUrls;
    }

    public ResponseEntity<String> getImageUrl(String publicId){
        return ResponseEntity.ok(cloudinary.url().resourceType("image").publicId(publicId).generate());
    }
}

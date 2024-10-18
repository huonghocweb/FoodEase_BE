package poly.foodease.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadFileService {
    public String uploadFile(MultipartFile file);
}

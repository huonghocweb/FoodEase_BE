package poly.foodease.ServiceImpl;

import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Service.UploadFileService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

@Service
public class UploadUploadServiceImpl implements UploadFileService {

    @Autowired
    ServletContext app;
    @Override
    public String uploadFile(MultipartFile file) {
        String uploadRootPath = Paths.get("D:\\HK7\\FoodEaseApp\\foodease-app\\public\\assets\\images").toAbsolutePath().toString();
        File uploadRootDir=new File(uploadRootPath);
        System.out.println(uploadRootPath);
        if(!uploadRootDir.exists())
        {
            uploadRootDir.mkdir();
        }
        try {
            String filename=file.getOriginalFilename();
            File serverFile=new File(uploadRootDir.getAbsoluteFile()+File.separator+filename);
            BufferedOutputStream stream=new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(file.getBytes());
            stream.close();
            return filename;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }

    }
}

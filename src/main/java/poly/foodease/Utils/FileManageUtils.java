package poly.foodease.Utils;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileManageUtils {
    private static final String BASE_PATH = "storage/foodease-app/images";
    private Path getPath(String folder, String filename){
        File dir= Paths.get(BASE_PATH,folder).toFile();
        if(!dir.exists()){
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(),filename);
    }
    public byte[] read(String folder, String filename){
        Path path= this.getPath(folder,  filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String folder, String filename){
        Path path= this.getPath(folder, filename);
        path.toFile().delete();
    }

    public List<String> list(String folder){
        List<String> filenames= new ArrayList<>();
        File dir= Paths.get(BASE_PATH,folder).toFile();
        if(!dir.exists()){
            File [] files = dir.listFiles();
            for(File file : files){
                filenames.add(file.getName());
            }
        }
        return filenames;
    }

    public List<String> save(String folder, MultipartFile[] files) throws IOException {
        List<String> filenames= new ArrayList<>();
        for(MultipartFile file : files){
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename= Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path= this.getPath(folder, filename);
            file.transferTo(path);
            filenames.add(filename);
            System.out.println(path);
        }
        return filenames;
    }
}


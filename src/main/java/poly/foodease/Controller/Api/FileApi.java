package poly.foodease.Controller.Api;


import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import poly.foodease.Utils.FileManageUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin("*")
@RestController
@RequestMapping("/api/files")
public class FileApi {

    @Autowired
    FileManageUtils fileManageUtils;

    @GetMapping("/{folder}/{files}")
    public byte[] download(@PathVariable("folder") String folder,@PathVariable("files") String file){
        return fileManageUtils.read(folder, file);
    }

    @PostMapping("/{folder}")
    public List<String> upload(@PathVariable("folder")String folder, @PathParam("files")MultipartFile[] files) throws IOException {
        return fileManageUtils.save(folder, files);
    }

    @DeleteMapping("/{folder}/{file}")
    public void delete(@PathVariable("folder") String folder, @PathVariable("file") String file){
        fileManageUtils.delete(folder, file);
    }

    @GetMapping("/{folder}")
    public List<String> list(@PathVariable("folder") String folder){
        return fileManageUtils.list(folder);
    }
}


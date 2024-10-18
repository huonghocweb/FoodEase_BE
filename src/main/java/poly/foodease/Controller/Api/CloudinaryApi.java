package poly.foodease.Controller.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poly.foodease.Service.CloudinaryService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryApi {
    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/getImage")
    public ResponseEntity<String> getImageBySourceUrl(@RequestPart("imageUrl") String imageUrl){
        System.out.println("567");
        System.out.println("getImage" + imageUrl);
        return cloudinaryService.getImageUrl(imageUrl);
    }
}

package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Request.HashtagRequest;
import poly.foodease.Model.Response.HashtagResponse;
import poly.foodease.Service.CloudinaryService;
import poly.foodease.Service.HashtagService;

@RestController
@RequestMapping("/api/hashtag")
@CrossOrigin("*")
public class HashtagApi {


    @Autowired
    private HashtagService hashtagService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<HashtagResponse> createHashtag(@RequestBody
    HashtagRequest hashtagRequest) {
    return ResponseEntity.ok(hashtagService.createHashtag(hashtagRequest));
    }

    
    @GetMapping("get/{id}")
    public ResponseEntity<HashtagResponse> getHashtagById(@PathVariable("id")
    Integer hashtagId) {
    return ResponseEntity.ok(hashtagService.getHashtagById(hashtagId));
    }

    

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteHashtag(@PathVariable("id") Integer hashtagId) {
        hashtagService.deleteHashtag(hashtagId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HashtagResponse>> getHashtags() {
        return ResponseEntity.ok(hashtagService.getHashtags());
    }

    
}

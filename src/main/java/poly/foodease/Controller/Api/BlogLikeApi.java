package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Request.BlogLikeRequest;
import poly.foodease.Model.Response.BlogLikeResponse;
import poly.foodease.Service.BlogLikeService;
import poly.foodease.Service.CloudinaryService;

@RestController
@RequestMapping("/api/bloglike")
@CrossOrigin("*")
public class BlogLikeApi {

    @Autowired
    private BlogLikeService blogLikeService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<BlogLikeResponse> createBlogLike(@RequestBody BlogLikeRequest blogLikeRequest) {
        return ResponseEntity.ok(blogLikeService.createBlogLike(blogLikeRequest));
    }

    @PutMapping("likeOrDislike")
    public ResponseEntity<BlogLikeResponse> likeOrDislikeBlog(@RequestBody BlogLikeRequest blogLikeRequest) {
        return ResponseEntity.ok(blogLikeService.likeOrDislikeBlog(blogLikeRequest));
    }

    @GetMapping("get/{id}")
    public ResponseEntity<BlogLikeResponse> getBlogLikeById(@PathVariable("id") Integer likeId) {
        return ResponseEntity.ok(blogLikeService.getBlogLikeById(likeId));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBlogLike(@PathVariable("id") Integer likeId) {
        blogLikeService.deleteBlogLike(likeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{blogId}")
    public ResponseEntity<List<BlogLikeResponse>> getLikeByBlogId(@PathVariable("blogId") Integer blogId) {
        return ResponseEntity.ok(blogLikeService.getLikeByBlogId(blogId));
    }

    
}

package poly.foodease.Controller.Api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Request.BlogCommentRequest;
import poly.foodease.Model.Response.BlogCommentResponse;
import poly.foodease.Service.BlogCommentService;
import poly.foodease.Service.CloudinaryService;

@RestController
@RequestMapping("/api/blogcomments")
@CrossOrigin("*")
public class BlogCommentApi {

    @Autowired
    private BlogCommentService blogCommentService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/{folder}")
    public ResponseEntity<Object> createBlogComment(
            @PathVariable("folder") String folder,
            @RequestPart("blogCommentRequest") BlogCommentRequest blogCommentRequest,
            @RequestPart(value = "ImgBlogComment", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();
        blogCommentRequest.setImageURL("123");
        if (files != null) {
            blogCommentRequest.setImageURL(cloudinaryService.uploadFile(files, folder).get(0));
            // couponRequest.setImageUrl(fileManageUtils.save(folder,files).get(0));
        } else {
            System.out.println("file null");
            blogCommentRequest.setImageURL(null);
        }
        try {
            result.put("success", true);
            result.put("message", "Create Coupon");
            result.put("data", blogCommentService.createBlogComment(blogCommentRequest));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{folder}/{id}")
    public ResponseEntity<Object> updateBlogComment(
            @PathVariable("folder") String folder,
            @PathVariable("id") Integer commentId,
            @RequestPart("blogCommentRequest") BlogCommentRequest blogCommentRequest,
            @RequestPart(value = "ImgBlogComment", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (files == null) {
            System.out.println("file null");
            BlogCommentResponse blogCommentResponse = blogCommentService.getBlogCommentById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Not Found Blog Comment"));
            blogCommentRequest.setImageURL(blogCommentRequest.getImageURL());
        } else {
            blogCommentRequest.setImageURL(cloudinaryService.uploadFile(files, folder).get(0));
        }
        try {
            result.put("success", true);
            result.put("message", "Update Coupon");
            result.put("data", blogCommentService.updateBlogComment(commentId, blogCommentRequest));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Object> getBlogCommentById(@PathVariable("id") Integer commentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "Get ResTable By ResTableId");
            result.put("data", blogCommentService.getBlogCommentById(commentId));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBlogComment(@PathVariable("id") Integer commentId) {
        BlogCommentResponse blogCommentResponse = blogCommentService.getBlogCommentById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        blogCommentService.deleteBlogComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // @GetMapping
    // public ResponseEntity<List<BlogCommentResponse>> getAllBlogComment() {
    // return ResponseEntity.ok(blogCommentService.getAllBlogComment());
    // }

    @GetMapping("{blogId}")
    public ResponseEntity<List<BlogCommentResponse>> getCommentsByBlogId(@PathVariable("blogId") Integer blogId) {
        return ResponseEntity.ok(blogCommentService.getCommentsByBlogId(blogId));
    }
}

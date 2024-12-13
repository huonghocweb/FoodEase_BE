package poly.foodease.Controller.Api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import poly.foodease.Model.Entity.Blog;
import poly.foodease.Model.Request.BlogRequest;
import poly.foodease.Model.Response.BlogResponse;
import poly.foodease.Service.BlogService;
import poly.foodease.Service.CloudinaryService;

@RestController
@RequestMapping("/api/blog")
@CrossOrigin("*")
public class BlogApi {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CloudinaryService cloudinaryService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{folder}")
    public ResponseEntity<Object> createBlog(
            @PathVariable("folder") String folder,
            @RequestPart("blogRequest") BlogRequest blogRequest,
            @RequestPart(value = "ImgBlog", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();
        blogRequest.setImageURL("123");
        if (files != null) {
            blogRequest.setImageURL(cloudinaryService.uploadFile(files, folder).get(0));
        } else {
            System.out.println("file null");
            blogRequest.setImageURL(" ");
        }
        try {

            BlogResponse blogResponse = blogService.createBlog(blogRequest);
            // Lưu hashtags nếu có trong yêu cầu
            if (blogRequest.getHashtagIds() != null && !blogRequest.getHashtagIds().isEmpty()) {
                blogService.saveHashtags(blogResponse.getBlogId(), blogRequest.getHashtagIds());
            }

            result.put("success", true);
            result.put("message", "Create Blog");
            result.put("data", blogResponse);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{folder}/{id}")
    public ResponseEntity<Object> updateBlog(
            @PathVariable("folder") String folder,
            @PathVariable("id") Integer blogId,
            @RequestPart("blogRequest") BlogRequest blogRequest,
            @RequestPart(value = "ImgBlog", required = false) MultipartFile[] files) throws IOException {
        Map<String, Object> result = new HashMap<>();

        if (files == null) {
            System.out.println("file null");
            BlogResponse blogResponse = blogService.getBlogById(blogId)
                    .orElseThrow(() -> new EntityNotFoundException("Not Found Blog"));
            blogRequest.setImageURL(blogRequest.getImageURL());
        } else {
            blogRequest.setImageURL(cloudinaryService.uploadFile(files, folder).get(0));
        }
        System.out.println(blogRequest);
        try {

            BlogResponse blogResponse = blogService.updateBlog(blogId, blogRequest)
                    .orElseThrow(() -> new EntityNotFoundException("Not Found Blog"));

            // Cập nhật hashtags nếu có trong yêu cầu
            if (blogRequest.getHashtagIds() != null && !blogRequest.getHashtagIds().isEmpty()) {
                blogService.saveHashtags(blogId, blogRequest.getHashtagIds());
            }
            result.put("success", true);
            result.put("message", "Update Coupon");
            result.put("data", blogResponse );
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Object> getBlogById(@PathVariable("id") Integer blogId) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("message", "Get ResTable By ResTableId");
            result.put("data", blogService.getBlogById(blogId));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("data", null);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") Integer blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Blog>> getBlogsByCategory(@PathVariable Integer categoryId) {
        List<Blog> relatedBlogs = blogService.findBlogsByCategoryId(categoryId);
        return ResponseEntity.ok(relatedBlogs);
    }

}

package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Response.BlogCategoryResponse;
import poly.foodease.Service.BlogCategoryService;

@RestController
@RequestMapping("/api/blogcategories")
@CrossOrigin("*")
public class BlogCategoryApi {

    @Autowired
    private BlogCategoryService blogCategoryService ;

    @GetMapping("get/{id}")
    public ResponseEntity<BlogCategoryResponse> getBlogCategoryById(@PathVariable("id") Integer blogCategoryId) {
        return ResponseEntity.ok(blogCategoryService.getBlogCategoryById(blogCategoryId));
    }

    @GetMapping
    public ResponseEntity<List<BlogCategoryResponse>> getAllBLogCategory() {
        return ResponseEntity.ok(blogCategoryService.getAllBLogCategory());
    }
}

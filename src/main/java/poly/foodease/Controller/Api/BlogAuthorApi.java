package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Response.BlogAuthorResponse;
import poly.foodease.Service.BlogAuthorService;

@RestController
@RequestMapping("/api/blogauthor")
@CrossOrigin("*")
public class BlogAuthorApi {

    @Autowired
    private BlogAuthorService blogAuthorService  ;

    @GetMapping("get/{id}")
    public ResponseEntity<BlogAuthorResponse> getBlogAuthorById(@PathVariable("id") Integer blogAuthorId) {
        return ResponseEntity.ok(blogAuthorService.getBlogAuthorById(blogAuthorId));
    }

    @GetMapping
    public ResponseEntity<List<BlogAuthorResponse>> getAllBlogAuthor() {
        return ResponseEntity.ok(blogAuthorService.getAllBlogAuthor());
    }
}

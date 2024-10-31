package poly.foodease.Service;

import java.util.List;
import java.util.Optional;

import poly.foodease.Model.Entity.Blog;
import poly.foodease.Model.Request.BlogRequest;
import poly.foodease.Model.Response.BlogResponse;

public interface BlogService {
    BlogResponse createBlog(BlogRequest blogRequest);

    Optional<BlogResponse> updateBlog(Integer blogId, BlogRequest blogRequest);

    Optional<BlogResponse> getBlogById(Integer blogId);

    List<BlogResponse> getAllBlogs();

    void deleteBlog(Integer blogId);
    
    List<Blog> findBlogsByCategoryId(Integer categoryId);

    
}

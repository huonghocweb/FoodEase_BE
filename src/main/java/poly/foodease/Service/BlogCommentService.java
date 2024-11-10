package poly.foodease.Service;

import java.util.List;
import java.util.Optional;

import poly.foodease.Model.Request.BlogCommentRequest;
import poly.foodease.Model.Response.BlogCommentResponse;

public interface BlogCommentService {

    BlogCommentResponse createBlogComment(BlogCommentRequest blogCommentRequest);

    Optional<BlogCommentResponse> updateBlogComment(Integer commentId, BlogCommentRequest blogCommentRequest);

    Optional<BlogCommentResponse> getBlogCommentById(Integer commentId);

    void deleteBlogComment(Integer commentId);

    List<BlogCommentResponse> getAllBlogComment();
    
    List<BlogCommentResponse> getCommentsByBlogId(Integer blogId);

}

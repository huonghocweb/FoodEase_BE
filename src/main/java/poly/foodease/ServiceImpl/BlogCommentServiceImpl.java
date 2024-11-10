package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.BlogCommentMapper;
import poly.foodease.Model.Entity.BlogComment;
import poly.foodease.Model.Request.BlogCommentRequest;
import poly.foodease.Model.Response.BlogCommentResponse;
import poly.foodease.Repository.BlogCommentRepo;
import poly.foodease.Service.BlogCommentService;
import poly.foodease.Service.UserService;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Autowired
    private BlogCommentRepo blogCommentRepo;

    @Autowired
    private BlogCommentMapper blogCommentMapper;
    @Autowired
    private UserService userService;

    @Override
    public BlogCommentResponse createBlogComment(BlogCommentRequest blogCommentRequest) {
        BlogComment newComment = blogCommentMapper.convertReqToEn(blogCommentRequest);
        blogCommentRepo.save(newComment);
        return blogCommentMapper.convertEnToRes(newComment);
    }

    @Override
    public Optional<BlogCommentResponse> updateBlogComment(Integer commentId, BlogCommentRequest blogCommentRequest) {
        return Optional.of(blogCommentRepo.findById(commentId)
                .map(existingComment -> {
                    BlogComment blogComment = blogCommentMapper.convertReqToEn(blogCommentRequest);
                    blogComment.setCommentId(existingComment.getCommentId());
                    BlogComment updatedComment = blogCommentRepo.save(blogComment);
                    return blogCommentMapper.convertEnToRes(updatedComment);
                })
                .orElseThrow(() -> new EntityNotFoundException("not found Blog Comment")));
    }

    @Override
    public Optional<BlogCommentResponse> getBlogCommentById(Integer commentId) {
        BlogComment blogComment = blogCommentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Blog Comment"));
        return Optional.of(blogCommentMapper.convertEnToRes(blogComment));
    }

    @Override
    public void deleteBlogComment(Integer commentId) {
        BlogComment blogComment = blogCommentRepo.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Table not found"));
        blogCommentRepo.delete(blogComment);
    }

    @Override
    public List<BlogCommentResponse> getAllBlogComment() {
        return blogCommentRepo.findAll().stream()
                .map(blogCommentMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogCommentResponse> getCommentsByBlogId(Integer blogId) {
        return blogCommentRepo.findByBlog_BlogId(blogId)
                .stream()
                .map(blogCommentMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}

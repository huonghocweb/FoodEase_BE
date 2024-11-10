package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.BlogComment;
import poly.foodease.Model.Request.BlogCommentRequest;
import poly.foodease.Model.Response.BlogCommentResponse;
import poly.foodease.Repository.BlogRepo;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class BlogCommentMapper {

        @Autowired
        private BlogMapper blogMapper;
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private BlogRepo blogRepo;
        @Autowired
        private UserRepo userRepo;

        public BlogCommentResponse convertEnToRes(BlogComment blogComment) {
                return BlogCommentResponse.builder()
                                .commentId(blogComment.getCommentId())
                                .commentContent(blogComment.getCommentContent())
                                .imageURL(blogComment.getImageURL() != null ? blogComment.getImageURL() : "")
                                .createAt(blogComment.getCreateAt())
                                .blog(blogMapper.convertEnToRes(blogComment.getBlog())) // Liên kết với BlogResponse
                                .user(userMapper.convertEnToRes(blogComment.getUser())) // Liên kết với UserResponse
                                .build();
        }

        public BlogComment convertReqToEn(BlogCommentRequest blogCommentRequest) {
                return BlogComment.builder()
                                .commentContent(blogCommentRequest.getCommentContent())
                                .imageURL(blogCommentRequest.getImageURL() != null ? blogCommentRequest.getImageURL()
                                                : "")
                                .createAt(blogCommentRequest.getCreateAt().now())
                                .blog(blogRepo.findById(blogCommentRequest.getBlogId())
                                                .orElseThrow(() -> new EntityNotFoundException("not found Blog")))
                                .user(userRepo.findById(blogCommentRequest.getUserId())
                                                .orElseThrow(() -> new EntityNotFoundException("not found User")))
                                .build();
        }
}

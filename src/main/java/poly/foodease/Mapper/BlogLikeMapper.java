package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.BlogLike;
import poly.foodease.Model.Request.BlogLikeRequest;
import poly.foodease.Model.Response.BlogLikeResponse;
import poly.foodease.Repository.BlogRepo;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class BlogLikeMapper {

        @Autowired
        private BlogMapper blogMapper;
        @Autowired
        private UserMapper userMapper;
        @Autowired
        private BlogRepo blogRepo;
        @Autowired
        private UserRepo userRepo;

        public BlogLikeResponse convertEnToRes(BlogLike blogLike) {
                return BlogLikeResponse.builder()
                                .likeId(blogLike.getLikeId())
                                .isLike(blogLike.getIsLike())
                                .blog(blogMapper.convertEnToRes(blogLike.getBlog())) // Liên kết với BlogResponse
                                .user(userMapper.convertEnToRes(blogLike.getUser())) // Liên kết với UserResponse
                                .build();
        }

        public BlogLike convertReqToEn(BlogLikeRequest blogLikeRequest) {
                return BlogLike.builder()
                                .isLike(blogLikeRequest.getIsLike())
                                .blog(blogRepo.findById(blogLikeRequest.getBlogId())
                                                .orElseThrow(() -> new EntityNotFoundException("not found Blog")))
                                .user(userRepo.findById(blogLikeRequest.getUserId())
                                                .orElseThrow(() -> new EntityNotFoundException("not found User")))
                                .build();
        }
}

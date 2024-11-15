package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import poly.foodease.Model.Entity.BlogLike;
import poly.foodease.Model.Request.BlogLikeRequest;
import poly.foodease.Model.Response.BlogLikeResponse;

@Mapper(componentModel = "spring")
public abstract class BlogLikeMapper {

        @Autowired
        private BlogMapper blogMapper;
        @Autowired
        private UserMapper userMapper;

        // Chuyển đối tượng BlogLike từ Entity sang Response
        public BlogLikeResponse convertEnToRes(BlogLike blogLike) {
                return BlogLikeResponse.builder()
                                .likeId(blogLike.getLikeId())
                                .isLike(blogLike.getIsLike())
                                .blog(blogMapper.convertEnToRes(blogLike.getBlog())) // Liên kết với BlogResponse
                                .user(userMapper.convertEnToRes(blogLike.getUser())) // Liên kết với UserResponse
                                .build();
        }

        // Chuyển đối tượng BlogLikeRequest thành BlogLike Entity
        public BlogLike convertReqToEn(BlogLikeRequest blogLikeRequest) {
                BlogLike blogLike = new BlogLike();
                blogLike.setIsLike(blogLikeRequest.getIsLike());
                // Blog và User sẽ được thiết lập trong service, không ánh xạ trực tiếp ở đây
                return blogLike;
        }
}

package poly.foodease.Mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.Blog;
import poly.foodease.Model.Request.BlogRequest;
import poly.foodease.Model.Response.BlogResponse;
import poly.foodease.Repository.BlogAuthorRepo;
import poly.foodease.Repository.BlogCategoryRepo;

@Mapper(componentModel = "spring")
public abstract class BlogMapper {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;
    @Autowired
    private BlogAuthorMapper blogAuthorMapper;
    @Autowired
    private HashtagMapper hashtagMapper;
    @Autowired
    private BlogCategoryRepo blogCategoryRepo;
    @Autowired
    private BlogAuthorRepo blogAuthorRepo;

    public BlogResponse convertEnToRes(Blog blog) {
        return BlogResponse.builder()
                .blogId(blog.getBlogId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .imageURL(blog.getImageURL())
                .createAt(blog.getCreateAt())
                .blogCategory(blogCategoryMapper.convertEnToRes(blog.getBlogCategory()))
                .blogAuthor(blogAuthorMapper.convertEnToRes(blog.getBlogAuthor()))
                .hashtags(blog.getHashtags().stream()
                        .map(hashtagMapper::convertEnToRes)
                        .toList()) // Chuyển đổi danh sách hashtag 
                .build();
    }

    public Blog convertReqToEn(BlogRequest blogRequest) {
        return Blog.builder()
                .title(blogRequest.getTitle())
                .content(blogRequest.getContent())
                .imageURL(blogRequest.getImageURL() != null ? blogRequest.getImageURL() : "")
                .createAt(blogRequest.getCreateAt().now()) 
                .blogCategory(blogCategoryRepo.findById(blogRequest.getBlogCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("not found Blog Category")))
                .blogAuthor(blogAuthorRepo.findById(blogRequest.getBlogAuthorId())
                        .orElseThrow(() -> new EntityNotFoundException("not found Blog Author")))
                .build();
    }
}

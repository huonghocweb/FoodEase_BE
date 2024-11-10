package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.BlogMapper;
import poly.foodease.Model.Entity.Blog;
import poly.foodease.Model.Entity.Hashtag;
import poly.foodease.Model.Request.BlogRequest;
import poly.foodease.Model.Response.BlogResponse;
import poly.foodease.Repository.BlogRepo;
import poly.foodease.Repository.HashtagRepo;
import poly.foodease.Service.BlogService;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private HashtagRepo hashtagRepo;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public BlogResponse createBlog(BlogRequest blogRequest) {
        Blog newBlog = blogMapper.convertReqToEn(blogRequest);
        // Lưu hashtags nếu có hashtagIds trong blogRequest
        if (blogRequest.getHashtagIds() != null && !blogRequest.getHashtagIds().isEmpty()) {
            List<Hashtag> hashtags = hashtagRepo.findAllById(blogRequest.getHashtagIds());
            newBlog.setHashtags(hashtags); // Giả sử Blog entity có phương thức setHashtags
        }
        blogRepo.save(newBlog);
        return blogMapper.convertEnToRes(newBlog);
    }

    @Override
    public Optional<BlogResponse> updateBlog(Integer blogId, BlogRequest blogRequest) {
        return Optional.of(blogRepo.findById(blogId)
                .map(existingBlog -> {
                    Blog blog = blogMapper.convertReqToEn(blogRequest);
                    blog.setBlogId(existingBlog.getBlogId());

                    // Cập nhật hashtags nếu có hashtagIds trong blogRequest
                    if (blogRequest.getHashtagIds() != null && !blogRequest.getHashtagIds().isEmpty()) {
                        List<Hashtag> hashtags = hashtagRepo.findAllById(blogRequest.getHashtagIds());
                        blog.setHashtags(hashtags); // Giả sử Blog entity có phương thức setHashtags
                    } else {
                        blog.setHashtags(existingBlog.getHashtags());
                    }

                    Blog updatedBlog = blogRepo.save(blog);
                    return blogMapper.convertEnToRes(updatedBlog);
                })
                .orElseThrow(() -> new EntityNotFoundException("not found Blog")));
    }

    @Override
    public void saveHashtags(Integer blogId, List<Integer> hashtagIds) {
        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found"));

        List<Hashtag> hashtags = hashtagRepo.findAllById(hashtagIds);
        blog.setHashtags(hashtags); // Giả sử Blog entity có phương thức setHashtags

        blogRepo.save(blog);
    }

    @Override
    public Optional<BlogResponse> getBlogById(Integer blogId) {
        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Not found Blog"));
        return Optional.of(blogMapper.convertEnToRes(blog));
    }

    @Override
    public void deleteBlog(Integer blogId) {
        Blog blog = blogRepo.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found"));
        blogRepo.delete(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        return blogRepo.findAll().stream()
                .map(blogMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<Blog> findBlogsByCategoryId(Integer categoryId) {
        return blogRepo.findByBlogCategory_BlogCategoryId(categoryId);
    }
}

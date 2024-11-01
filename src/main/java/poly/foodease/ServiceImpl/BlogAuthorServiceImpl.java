package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.BlogAuthorMapper;
import poly.foodease.Model.Entity.BlogAuthor;
import poly.foodease.Model.Response.BlogAuthorResponse;
import poly.foodease.Repository.BlogAuthorRepo;
import poly.foodease.Service.BlogAuthorService;

@Service
public class BlogAuthorServiceImpl implements BlogAuthorService {

    @Autowired
    private BlogAuthorRepo blogAuthorRepo;

    @Autowired
    private BlogAuthorMapper blogAuthorMapper;

    @Override
    public BlogAuthorResponse getBlogAuthorById(Integer blogAuthorId) {
        BlogAuthor blogAuthor = blogAuthorRepo.findById(blogAuthorId)
                .orElseThrow(() -> new EntityNotFoundException("Blog author not found"));
        return blogAuthorMapper.convertEnToRes(blogAuthor);
    }

    @Override
    public List<BlogAuthorResponse> getAllBlogAuthor() {
        return blogAuthorRepo.findAll().stream()
                .map(blogAuthorMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}

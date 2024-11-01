package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.BlogCategoryMapper;
import poly.foodease.Model.Entity.BlogCategory;
import poly.foodease.Model.Response.BlogCategoryResponse;
import poly.foodease.Repository.BlogCategoryRepo;
import poly.foodease.Service.BlogCategoryService;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    private BlogCategoryRepo blogCategoryRepo;

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public BlogCategoryResponse getBlogCategoryById(Integer blogCategoryId) {
        BlogCategory blogCategory = blogCategoryRepo.findById(blogCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Blog category not found"));
        return blogCategoryMapper.convertEnToRes(blogCategory);
    }

    @Override
    public List<BlogCategoryResponse> getAllBLogCategory() {
        return blogCategoryRepo.findAll().stream()
                .map(blogCategoryMapper::convertEnToRes)
                .collect(Collectors.toList());
    }
}

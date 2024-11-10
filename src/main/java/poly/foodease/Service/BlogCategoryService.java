package poly.foodease.Service;
import java.util.List;

import poly.foodease.Model.Response.BlogCategoryResponse;

public interface BlogCategoryService {
    BlogCategoryResponse getBlogCategoryById(Integer blogCategoryId);

    List<BlogCategoryResponse> getAllBLogCategory();
}

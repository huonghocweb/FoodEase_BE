package poly.foodease.Service;
import java.util.List;

import poly.foodease.Model.Response.BlogAuthorResponse;

public interface BlogAuthorService {
    BlogAuthorResponse getBlogAuthorById(Integer blogAuthorId);

    List<BlogAuthorResponse> getAllBlogAuthor();
}

package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Request.BlogLikeRequest;
import poly.foodease.Model.Response.BlogLikeResponse;

public interface BlogLikeService {

    BlogLikeResponse createBlogLike(BlogLikeRequest blogLikeRequest);

    BlogLikeResponse likeOrDislikeBlog( BlogLikeRequest blogLikeRequest);

    // Optional<ResTableResponse> updateResTableNew(Integer tableId, ResTableRequest
    // resTableRequest);

    BlogLikeResponse getBlogLikeById(Integer likeId);

    // Optional<ResTableResponse> getResTableByIdNew(Integer tableId);

    void deleteBlogLike(Integer likeId);

    List<BlogLikeResponse> getAllBlogLike();

     List<BlogLikeResponse> getLikeByBlogId(Integer blogId);
}

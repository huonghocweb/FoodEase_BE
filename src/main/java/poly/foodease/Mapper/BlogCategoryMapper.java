package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.BlogCategory;
import poly.foodease.Model.Response.BlogCategoryResponse;

@Mapper(componentModel = "spring")
public abstract class BlogCategoryMapper {

    public BlogCategoryResponse convertEnToRes(BlogCategory blogCategory) {
        return BlogCategoryResponse.builder()
                .blogCategoryId(blogCategory.getBlogCategoryId())
                .blogCategoryName(blogCategory.getBlogCategoryName())
                .build();
    }
}

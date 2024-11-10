package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.BlogAuthor;
import poly.foodease.Model.Response.BlogAuthorResponse;

@Mapper(componentModel = "spring")
public abstract class BlogAuthorMapper {

    public BlogAuthorResponse convertEnToRes(BlogAuthor blogAuthor) {
        return BlogAuthorResponse.builder()
                .blogAuthorId(blogAuthor.getBlogAuthorId())
                .blogAuthorName(blogAuthor.getBlogAuthorName())
                .build();
    }
}

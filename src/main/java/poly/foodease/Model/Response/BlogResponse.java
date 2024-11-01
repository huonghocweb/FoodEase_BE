package poly.foodease.Model.Response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogResponse {
    private Integer blogId;
    private String title;
    private String content;
    private String imageURL;
    private LocalDateTime createAt;
    private BlogCategoryResponse blogCategory;
    private BlogAuthorResponse blogAuthor;
    private List<HashtagResponse> hashtags;
    private Integer likeCount;
    private List<BlogCommentResponse> comments;
}

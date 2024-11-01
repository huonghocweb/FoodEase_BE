package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogLikeResponse {
    private Integer likeId;
    private Boolean isLike;
    private BlogResponse blog; // Liên kết với BlogResponse
    private UserResponse user; // Liên kết với UserResponse
}

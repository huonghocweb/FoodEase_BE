package poly.foodease.Model.Response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogCommentResponse {
    private Integer commentId;
    private String commentContent;
    private String imageURL;
    private LocalDateTime createAt;
    private BlogResponse blog; // Liên kết với BlogResponse
    private UserResponse user; // Liên kết với UserResponse
}

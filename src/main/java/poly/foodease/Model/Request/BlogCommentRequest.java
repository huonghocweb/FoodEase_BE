package poly.foodease.Model.Request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogCommentRequest {
    private String commentContent;
    private LocalDateTime createAt; // Nếu cần thiết
    private Integer blogId; // Để liên kết với Blog
    private Integer userId; // Để liên kết với User
}

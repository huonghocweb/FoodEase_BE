package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogLikeRequest {
    private Boolean isLike; // 1 là like, 0 là unlike
    private Integer blogId; // Để liên kết với Blog
    private Integer userId; // Để liên kết với User
}

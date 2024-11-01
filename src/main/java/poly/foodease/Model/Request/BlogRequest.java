package poly.foodease.Model.Request;

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
public class BlogRequest {
    private String title;
    private String content;
    private String imageURL;
    private LocalDateTime createAt; 
    private Integer blogCategoryId; 
    private Integer blogAuthorId; 
    private List<Integer> hashtagIds;
}

package poly.foodease.Model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FoodReviewRequest {

    private int rating;
    private String review;
    private int foodId;
    private int userId;
    private MultipartFile image;

}

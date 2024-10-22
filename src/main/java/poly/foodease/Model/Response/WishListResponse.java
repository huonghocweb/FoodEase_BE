package poly.foodease.Model.Response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListResponse {
    private Integer wishListId;
    private String wishListName;
    private UserResponse userResponse;
    private List<FoodResponse> foodResponse;
}

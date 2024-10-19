package poly.foodease.Model.Response;

import java.util.List;

import lombok.Data;

@Data
public class WishListResponse {
    private Integer wishListId;
    private Integer userId;
    private String wishListName;
    private List<FoodResponse> foods; // Hoặc danh sách các đối tượng Foods nếu cần
    private Integer foodId; // Hoặc danh sách các đối tượng Foods nếu cần
    // thay đổi
    private List<FoodVariationResponse> foodVariations;
    private Integer foodVariationId; // Thêm trường foodVariationId
    private Integer quantityStock;
}

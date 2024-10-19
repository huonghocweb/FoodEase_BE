package poly.foodease.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.WishList;
import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodVariationResponse;
import poly.foodease.Model.Response.WishListResponse;

@Component
public class WishListMapper {

    public WishList toEntity(WishListRequest request) {
        WishList wishList = new WishList();
        wishList.setWishListName(request.getWishListName());
        return wishList;
    }

    public WishListResponse toResponse(WishList wishList) {
        WishListResponse response = new WishListResponse();
        response.setWishListId(wishList.getWishListId());
        response.setWishListName(wishList.getWishListName());

        // Danh sách để chứa thông tin các FoodVariationResponse
        List<FoodVariationResponse> foodVariationResponses = new ArrayList<>();

        // Kiểm tra xem wishlist có món ăn không
        if (wishList.getFoods() != null) {
            for (Foods food : wishList.getFoods()) {
                // Giả sử food có một phương thức để lấy danh sách các FoodVariations
                List<FoodVariations> foodVariations = food.getFoodVariations(); // Thay đổi theo cách bạn thiết lập quan
                                                                                // hệ

                // Duyệt qua từng FoodVariations để lấy thông tin
                for (FoodVariations foodVariation : foodVariations) {
                    FoodVariationResponse foodVariationResponse = new FoodVariationResponse();
                    foodVariationResponse.setFoodVariationId(foodVariation.getFoodVariationId());
                    foodVariationResponse.setQuantityStock(foodVariation.getQuantityStock());

                    // Thêm vào danh sách
                    foodVariationResponses.add(foodVariationResponse);
                }
            }
        }

        response.setFoodVariations(foodVariationResponses); // Thêm vào response
        return response;
    }

    public WishListRequest toRequest(WishList wishList) {
        WishListRequest request = new WishListRequest();
        request.setWishListName(wishList.getWishListName());
        return request;
    }

}

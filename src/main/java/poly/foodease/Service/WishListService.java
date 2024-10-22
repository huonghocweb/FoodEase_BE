package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;

public interface WishListService {
    WishListResponse createWishList(WishListRequest wishListRequest);

    WishListResponse updateWishList(Integer wishListId, WishListRequest wishListRequest);

    List<FoodResponse> getFoodsByWishListId(Integer wishListId);

    void deleteWishList(Integer wishListId);

    List<WishListResponse> getWishListsByUserId(Integer userId);

    WishListResponse addFoodToWishList(Integer wishListId, Integer foodId);

    WishListResponse removeFoodFromWishList(Integer wishListId, Integer foodId);

    boolean isFoodInWishList(Integer wishListId, Integer foodId);
}

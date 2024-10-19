package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;

public interface WishListService {
    WishListResponse createWishList(Integer userId, WishListRequest request);

    WishListResponse renameWishList(Integer wishListId, WishListRequest request);

    void deleteWishList(Integer wishListId);

    List<WishListResponse> getWishListsByUser(Integer userId);

    List<FoodResponse> getFoodsByWishList(Integer wishListId);

    WishListResponse addFoodToWishList(Integer wishListId, Integer foodId, Integer foodVariationId,
            Integer quantityStock);

    boolean isFoodInWishList(Integer wishListId, Integer foodId);

    void removeFoodFromWishList(Integer wishListId, Integer foodId);

    FoodResponse getFoodInWishList(Integer wishListId, Integer foodId);

}

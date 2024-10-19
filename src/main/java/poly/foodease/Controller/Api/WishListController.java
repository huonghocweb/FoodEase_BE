package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Request.FoodWishListRequest;
import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;
import poly.foodease.Service.WishListService;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @PostMapping("/{wishListId}/add-food/{foodId}")
    public ResponseEntity<WishListResponse> addFoodToWishList(
            @PathVariable Integer wishListId,
            @PathVariable Integer foodId,
            @RequestBody FoodWishListRequest request) {
        return ResponseEntity.ok(wishListService.addFoodToWishList(wishListId, foodId, request.getFoodVariationId(),
                request.getQuantityStock()));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<WishListResponse> createWishList(@PathVariable Integer userId,
            @RequestBody WishListRequest request) {
        return ResponseEntity.ok(wishListService.createWishList(userId, request));
    }

    @PutMapping("rename/{wishListId}")
    public ResponseEntity<WishListResponse> renameWishList(@PathVariable Integer wishListId,
            @RequestBody WishListRequest request) {
        return ResponseEntity.ok(wishListService.renameWishList(wishListId, request));
    }

    @DeleteMapping("delete/{wishListId}")
    public ResponseEntity<Void> deleteWishList(@PathVariable Integer wishListId) {
        wishListService.deleteWishList(wishListId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("get-wishlist/user/{userId}")
    public ResponseEntity<List<WishListResponse>> getWishListsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(wishListService.getWishListsByUser(userId));
    }

    @GetMapping("/{wishListId}/foods")
    public ResponseEntity<List<FoodResponse>> getFoodsByWishList(@PathVariable Integer wishListId) {
        List<FoodResponse> foods = wishListService.getFoodsByWishList(wishListId);
        return ResponseEntity.ok(foods);
    }

    @GetMapping("/{wishListId}/foods/{foodId}")
    public ResponseEntity<FoodResponse> getFoodInWishList(@PathVariable Integer wishListId,
            @PathVariable Integer foodId) {
        FoodResponse foodResponse = wishListService.getFoodInWishList(wishListId, foodId);
        return ResponseEntity.ok(foodResponse);
    }

    @GetMapping("/{wishListId}/contains-food/{foodId}")
    public ResponseEntity<Boolean> isFoodInWishList(@PathVariable Integer wishListId, @PathVariable Integer foodId) {
        boolean exists = wishListService.isFoodInWishList(wishListId, foodId);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{wishListId}/remove-food/{foodId}")
    public ResponseEntity<Void> removeFoodFromWishList(@PathVariable Integer wishListId, @PathVariable Integer foodId) {
        wishListService.removeFoodFromWishList(wishListId, foodId);
        return ResponseEntity.noContent().build();
    }
}

package poly.foodease.Controller.Api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;
import poly.foodease.Service.WishListService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/wishlist")
public class WishListApi {

    @Autowired
    private WishListService wishListService;

    @PostMapping("/create")
    public ResponseEntity<WishListResponse> createWishList(@RequestBody WishListRequest wishListRequest) {
        WishListResponse response = wishListService.createWishList(wishListRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{wishListId}")
    public ResponseEntity<WishListResponse> updateWishList(@PathVariable Integer wishListId,
            @RequestBody WishListRequest wishListRequest) {
        WishListResponse response = wishListService.updateWishList(wishListId, wishListRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{wishListId}")
    public ResponseEntity<Void> deleteWishList(@PathVariable Integer wishListId) {
        wishListService.deleteWishList(wishListId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{wishListId}/foods")
    public ResponseEntity<List<FoodResponse>> getFoodsByWishListId(@PathVariable Integer wishListId) {
        List<FoodResponse> response = wishListService.getFoodsByWishListId(wishListId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishListResponse>> getWishListsByUserId(@PathVariable Integer userId) {
        List<WishListResponse> response = wishListService.getWishListsByUserId(userId);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/{wishListId}/add-food/{foodId}")
    public ResponseEntity<WishListResponse> addFoodToWishList(@PathVariable Integer wishListId,
            @PathVariable Integer foodId) {
        WishListResponse response = wishListService.addFoodToWishList(wishListId, foodId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{wishListId}/contains-food/{foodId}")
    public ResponseEntity<Boolean> isFoodInWishList(@PathVariable Integer wishListId,
            @PathVariable Integer foodId) {
        boolean exists = wishListService.isFoodInWishList(wishListId, foodId);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{wishListId}/remove-food/{foodId}")
    public ResponseEntity<WishListResponse> removeFoodFromWishList(@PathVariable Integer wishListId,
            @PathVariable Integer foodId) {
        WishListResponse response = wishListService.removeFoodFromWishList(wishListId, foodId);
        return ResponseEntity.ok(response);
    }
}

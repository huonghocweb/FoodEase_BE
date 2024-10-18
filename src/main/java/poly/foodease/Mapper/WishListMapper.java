package poly.foodease.Mapper;

import org.springframework.stereotype.Component;

import poly.foodease.Model.Entity.WishList;
import poly.foodease.Model.Request.WishListRequest;
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
        return response;
    }

    public WishListRequest toRequest(WishList wishList) {
        WishListRequest request = new WishListRequest();
        request.setWishListName(wishList.getWishListName());
        return request;
    }

    
}

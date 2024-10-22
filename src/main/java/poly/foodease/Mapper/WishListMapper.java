package poly.foodease.Mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Model.Entity.WishList;
import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.WishListResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Repository.UserRepo;

@Mapper(componentModel = "spring")
public abstract class WishListMapper {
        @Autowired
        private FoodMapper foodMapper;
        @Autowired
        private FoodsDao foodsDao;
        @Autowired
        private UserRepo userRepo;
        @Autowired
        private UserMapper userMapper;

        public WishListResponse convertEnToRes(WishList wishList) {
                return WishListResponse.builder()
                                .wishListId(wishList.getWishListId())
                                .wishListName(wishList.getWishListName())
                                .userResponse(userMapper.convertEnToRes(wishList.getUser()))
                                .foodResponse(wishList.getFoods().stream()
                                                .map(foodMapper::converEntoResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public WishList convertReqToEn(WishListRequest wishListRequest) {
                return WishList.builder()
                                .wishListName(wishListRequest.getWishListName())
                                .user(userRepo.findById(wishListRequest.getUserId())
                                                .orElseThrow(() -> new EntityNotFoundException("Not found User")))
                                .foods(wishListRequest.getFoodId().stream()
                                                .map(foodId -> foodsDao.findById(foodId)
                                                                .orElseThrow(() -> new EntityNotFoundException(
                                                                                "not found Foods")))
                                                .collect(Collectors.toList()))
                                .build();
        }
}

package poly.foodease.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.FoodMapper;
import poly.foodease.Mapper.WishListMapper;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.WishList;
import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Repository.WishListRepo;
import poly.foodease.Service.WishListService;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepo wishListRepository;

    @Autowired
    private WishListMapper wishListMapper;

    @Autowired
    private FoodsDao foodsDao;
    @Autowired
    private FoodMapper foodMapper;

    @Override
    public WishListResponse createWishList(WishListRequest wishListRequest) {
        WishList wishList = wishListMapper.convertReqToEn(wishListRequest);
        wishList = wishListRepository.save(wishList);
        return wishListMapper.convertEnToRes(wishList);
    }

    @Override
    public WishListResponse updateWishList(Integer wishListId, WishListRequest wishListRequest) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
        wishList.setWishListName(wishListRequest.getWishListName());
        wishList = wishListRepository.save(wishList);
        return wishListMapper.convertEnToRes(wishList);
    }

    @Override
    public void deleteWishList(Integer wishListId) {
        wishListRepository.deleteById(wishListId);
    }

    @Override
    public List<FoodResponse> getFoodsByWishListId(Integer wishListId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
        return wishList.getFoods().stream()
                .map(foodMapper::converEntoResponse) // Giả sử bạn có một foodMapper để chuyển đổi sang FoodResponse
                .collect(Collectors.toList());
    }

    @Override
    public List<WishListResponse> getWishListsByUserId(Integer userId) {
        List<WishList> wishLists = wishListRepository.findAllByUser_UserId(userId);
        return wishLists.stream()
                .map(wishListMapper::convertEnToRes)
                .collect(Collectors.toList());
    }

    @Override
    public WishListResponse addFoodToWishList(Integer wishListId, Integer foodId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
        Foods food = foodsDao.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food not found"));
        wishList.getFoods().add(food);
        wishList = wishListRepository.save(wishList);
        return wishListMapper.convertEnToRes(wishList);
    }

    @Override
    public boolean isFoodInWishList(Integer wishListId, Integer foodId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
        return wishList.getFoods().stream().anyMatch(food -> food.getFoodId()==(foodId));
    }

    @Override
    public WishListResponse removeFoodFromWishList(Integer wishListId, Integer foodId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
        Foods food = foodsDao.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food not found"));
        wishList.getFoods().remove(food);
        wishList = wishListRepository.save(wishList);
        return wishListMapper.convertEnToRes(wishList);
    }
}

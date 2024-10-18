package poly.foodease.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.FoodMapper;
import poly.foodease.Mapper.WishListMapper;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Entity.WishList;
import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;
import poly.foodease.Repository.FoodsDao;
import poly.foodease.Repository.UserRepo;
import poly.foodease.Repository.WishListRepository;
import poly.foodease.Service.FoodsService;
import poly.foodease.Service.WishListService;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private WishListMapper wishListMapper;

    @Autowired
    private FoodsDao foodsDao;

    @Autowired

    private FoodMapper foodMapper;

    @Autowired
    private FoodsService foodsService;

    @Override
    public WishListResponse addFoodToWishList(Integer wishListId, Integer foodId) {
        // Tìm kiếm wishlist theo ID
        WishList wishList = wishListRepository.findByWishListId(wishListId);
        if (wishList == null) {
            throw new RuntimeException("WishList not found");
        }
        // Tìm kiếm món ăn theo ID
        Foods food = foodsDao.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food not found"));

        FoodResponse foodResponse = foodMapper.converEntoResponse(food);
        WishListRequest wishListRequest = wishListMapper.toRequest(wishList);

        if (wishList.getFoods() == null) {
            wishList.setFoods(new ArrayList<>());
        }

        wishList.getFoods().add(food);
        WishList updatedWishList = wishListRepository.save(wishList);
        return wishListMapper.toResponse(updatedWishList);
    }

    @Override
    public WishListResponse createWishList(Integer userId, WishListRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        WishList wishList = wishListMapper.toEntity(request);
        wishList.setUser(user);

        WishList savedWishList = wishListRepository.save(wishList);
        return wishListMapper.toResponse(savedWishList);
    }

    @Override
    public WishListResponse renameWishList(Integer wishListId, WishListRequest request) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new RuntimeException("WishList not found"));

        wishList.setWishListName(request.getWishListName());
        WishList updatedWishList = wishListRepository.save(wishList);
        return wishListMapper.toResponse(updatedWishList);
    }

    @Override
    public void deleteWishList(Integer wishListId) {
        wishListRepository.deleteById(wishListId);
    }

    @Override
    public List<WishListResponse> getWishListsByUser(Integer userId) {
        List<WishList> wishLists = wishListRepository.findByUserUserId(userId);
        return wishLists.stream()
                .map(wishListMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Foods> getFoodsByWishList(Integer wishListId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new RuntimeException("WishList not found"));
        return wishList.getFoods();
    }

    public boolean isFoodInWishList(Integer wishListId, Integer foodId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new RuntimeException("WishList not found"));
        return wishList.getFoods().stream()
                .anyMatch(food -> food.getFoodId() == (foodId));
    }

    public void removeFoodFromWishList(Integer wishListId, Integer foodId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        Foods food = foodsDao.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        wishList.getFoods().remove(food);
        wishListRepository.save(wishList);
    }
}

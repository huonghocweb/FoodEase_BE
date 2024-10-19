package poly.foodease.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import poly.foodease.Mapper.FoodMapper;
import poly.foodease.Mapper.WishListMapper;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Entity.User;
import poly.foodease.Model.Entity.WishList;
import poly.foodease.Model.Request.WishListRequest;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.WishListResponse;
import poly.foodease.Repository.FoodVariationsDao;
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

    @Autowired
    private FoodVariationsDao foodVariationRepo;

    @Override
    public WishListResponse addFoodToWishList(Integer wishListId, Integer foodId, Integer foodVariationId,
            Integer quantityStock) {
        // Tìm kiếm wishlist theo ID
        WishList wishList = wishListRepository.findByWishListId(wishListId);
        if (wishList == null) {
            throw new RuntimeException("WishList not found");
        }

        // Tìm kiếm món ăn theo ID
        Foods food = foodsDao.findById(foodId)
                .orElseThrow(() -> new EntityNotFoundException("Food not found"));

        // Tìm FoodVariation dựa trên foodVariationId
        FoodVariations foodVariation = foodVariationRepo.findById(foodVariationId)
                .orElseThrow(() -> new EntityNotFoundException("Food Variation not found"));

        // Kiểm tra xem foodVariation có hợp lệ không
        if (foodVariation.getQuantityStock() < quantityStock) {
            throw new RuntimeException("Not enough quantity in stock");
        }

        // Mapping dữ liệu
        FoodResponse foodResponse = foodMapper.converEntoResponse(food);
        WishListRequest wishListRequest = wishListMapper.toRequest(wishList);

        // Khởi tạo danh sách món ăn nếu chưa có
        if (wishList.getFoods() == null) {
            wishList.setFoods(new ArrayList<>());
        }

        // Thêm món ăn vào danh sách (chỉ thêm food)
        wishList.getFoods().add(food); // Thay đổi ở đây

        // Thêm thông tin foodVariation vào wishlist nếu cần thiết
        // (Có thể lưu foodVariationId và quantityStock vào một thuộc tính khác nếu cần)

        WishList updatedWishList = wishListRepository.save(wishList);

        // Đảm bảo bạn trả về thông tin foodVariationId và quantityStock trong response
        WishListResponse wishListResponse = wishListMapper.toResponse(updatedWishList);
        wishListResponse.setFoodVariationId(foodVariation.getFoodVariationId());
        wishListResponse.setQuantityStock(foodVariation.getQuantityStock());

        return wishListResponse;
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
    public List<FoodResponse> getFoodsByWishList(Integer wishListId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new RuntimeException("WishList not found"));

        // Ánh xạ danh sách Foods sang FoodResponse
        return wishList.getFoods().stream()
                .map(foodMapper::converEntoResponse) // Sử dụng foodMapper để ánh xạ
                .collect(Collectors.toList());
    }

    @Override
    public FoodResponse getFoodInWishList(Integer wishListId, Integer foodId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new RuntimeException("WishList not found"));

        // Kiểm tra xem món ăn có trong wishlist không
        Foods food = wishList.getFoods().stream()
                .filter(f -> f.getFoodId() == foodId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Food not found in WishList"));

        // Chuyển đổi sang FoodResponse
        return foodMapper.converEntoResponse(food);
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

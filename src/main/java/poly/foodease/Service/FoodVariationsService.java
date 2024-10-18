package poly.foodease.Service;

import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.FoodVariationResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FoodVariationsService {

//	List<FoodVariationResponse> findByCategoryMainDishes();
	Page<FoodVariationResponse> findByCategoryMainDishes(Pageable pageable);
	List<FoodVariationResponse> findByCategoryDrink();
	Optional<FoodVariationResponse> findById1(Integer id);
	Optional<FoodVariations> findById(Integer id);
	FoodVariationResponse findFoodVariationBySize(Integer id,String Size);
	List<FoodVariationResponse> findFoodVariationByFoodName (String foodName);
	List<FoodVariationResponse> findFoodVariationByCategoryId(Integer id);
	List<FoodVariationResponse> findFoodVariationByUserId(Integer id);
	Page<FoodVariationResponse> findAll(Pageable pageple);
}

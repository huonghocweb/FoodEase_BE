package poly.foodease.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Request.FoodRequest;
import poly.foodease.Model.Response.FoodResponse;



public interface FoodsService {

	List<FoodResponse> findAll();
	List<Foods> findByCategoryMainDishes();
	List<Foods> findByCategoryDrink();
	void deleteFood(int foodId);
	FoodResponse save (String name,String deception,Double pasePrice,MultipartFile file,Integer discout,Integer categoryId);
	Page<FoodResponse> findAddFoodAll(Pageable page);
	
}

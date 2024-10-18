package poly.foodease.Service;

import java.util.List;

import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Response.FoodResponse;



public interface FoodsService {

	List<FoodResponse> findAll();
	List<Foods> findByCategoryMainDishes();
	List<Foods> findByCategoryDrink();
	void deleteFood(int foodId);
	
}

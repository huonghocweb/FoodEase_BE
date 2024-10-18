package poly.foodease.Service;

import poly.foodease.Model.Entity.FoodVariationToppings;
import poly.foodease.Model.Response.FoodVariationToppingResponse;

import java.util.List;


public interface FoodVariationToppingService {

	List<FoodVariationToppingResponse> findFoodVariationToppingById(Integer id);
}

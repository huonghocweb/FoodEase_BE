package poly.foodease.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.FoodCategories;
import poly.foodease.Model.Response.FoodCategoriesReponse;

@Service
public interface FoodCategoryService {
	
	List<FoodCategoriesReponse> findAll();
	void deleteCategories(Integer id);
} 

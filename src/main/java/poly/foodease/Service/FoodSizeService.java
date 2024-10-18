package poly.foodease.Service;

import org.springframework.stereotype.Service;
import poly.foodease.Model.Entity.FoodSize;
import poly.foodease.Model.Response.FoodSizeResponse;

import java.util.List;

@Service
public interface FoodSizeService {
	FoodSizeResponse findFoodSizeBySize(String foodSizeName);
	List<FoodSizeResponse> findAll();
	List<FoodSizeResponse> findFoodSizeByFoodId(Integer id);
}

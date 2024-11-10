package poly.foodease.Service;

import poly.foodease.Model.Entity.FoodImage;
import poly.foodease.Model.Response.FoodImageResponse;

import java.util.List;


public interface FoodImageService {
List<FoodImageResponse> findFoodImageByFoodId(Integer id);
}

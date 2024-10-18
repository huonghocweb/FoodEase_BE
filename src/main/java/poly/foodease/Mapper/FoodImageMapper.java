package poly.foodease.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.OneToMany;
import poly.foodease.Model.Entity.FoodImage;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Response.FoodImageResponse;

@Mapper(componentModel = "Spring")
public abstract class FoodImageMapper {
	public FoodImageResponse converEntoResponse(FoodImage foodImage)
	{
		return FoodImageResponse.builder()
				.foodsImageId(foodImage.getFoodsImageId())
				.images(foodImage.getImages())
				.foodId(foodImage.getFoodId())
				.foods(foodImage.getFoods())
				.build();
	}
}

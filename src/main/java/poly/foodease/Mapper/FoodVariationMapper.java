package poly.foodease.Mapper;


import java.util.List;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.FoodSize;
import poly.foodease.Model.Entity.FoodVariationToppings;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Response.FoodVariationResponse;

@Mapper(componentModel ="Spring")
public abstract class FoodVariationMapper {
public FoodVariationResponse converEnToReponse(FoodVariations foodVariations)
	{
		return FoodVariationResponse.builder()
				.foodVariationId(foodVariations.getFoodVariationId())
				.imageUrl(foodVariations.getImageUrl())
				.createdAt(foodVariations.getCreatedAt())
				.updatedAt(foodVariations.getUpdatedAt())
				.quantityStock(foodVariations.getQuantityStock())
				.foodId(foodVariations.getFoodId())
				.foodSizeId(foodVariations.getFoodSizeId())
				.food(foodVariations.getFood())
				.foodSize(foodVariations.getFoodSize())
				.foodVariationToppings(foodVariations.getFoodVariationToppings())
				.build();
	}
}

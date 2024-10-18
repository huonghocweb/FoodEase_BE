package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.FoodVariationToppings;
import poly.foodease.Model.Response.FoodVariationToppingResponse;

@Mapper(componentModel = "Spring")
public abstract class FoodVariationToppingMapper {
	public FoodVariationToppingResponse converEntoResponse(FoodVariationToppings foodVariationToppings)
	{
		return FoodVariationToppingResponse.builder()
				.foodVariationId(foodVariationToppings.getFoodVariationId())
				.toppingId(foodVariationToppings.getToppingId())
				.foodVariations(foodVariationToppings.getFoodVariations())
				.toppings(foodVariationToppings.getToppings())
				.build();
	}
}
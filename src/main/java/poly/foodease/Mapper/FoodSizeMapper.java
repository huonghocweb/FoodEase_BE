package poly.foodease.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.FoodSize;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Response.FoodResponse;
import poly.foodease.Model.Response.FoodSizeResponse;

@Mapper(componentModel = "Spring")
public abstract class FoodSizeMapper {
	public 	FoodSizeResponse converEnToRespon(FoodSize foodSize)
	{
		return FoodSizeResponse.builder()
				.foodSizeId(foodSize.getFoodSizeId())
				.foodSizeName(foodSize.getFoodSizeName())
				.price(foodSize.getPrice())
				.foodVariations(foodSize.getFoodVariations()).build();
	}
	
}
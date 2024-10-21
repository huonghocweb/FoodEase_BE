package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import poly.foodease.Model.Entity.FoodCategories;
import poly.foodease.Model.Response.FoodCategoriesReponse;
import poly.foodease.Model.Response.FoodResponse;

@Mapper(componentModel = "spring")
public abstract class FoodCategoryMapper {
	public FoodCategoriesReponse converEnToRes(FoodCategories foodCategories)
	{
		return FoodCategoriesReponse.builder()
				.foodCategoriesID(foodCategories.getCategoryId())
				.foodCategoriesName(foodCategories.getCartegoryName()).build();
	}
}

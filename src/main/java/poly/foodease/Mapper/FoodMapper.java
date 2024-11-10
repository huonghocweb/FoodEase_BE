package poly.foodease.Mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import poly.foodease.Model.Entity.Foods;
import poly.foodease.Model.Request.FoodRequest;
import poly.foodease.Model.Response.FoodResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class FoodMapper {
public FoodResponse converEntoResponse(Foods foods) {
	return FoodResponse.builder()
			.foodId(foods.getFoodId())
			.foodName(foods.getFoodName())
			.description(foods.getDescription())
			.basePrice(foods.getBasePrice())
			.imageUrl(foods.getImageUrl())
			.createdAt(foods.getCreatedAt())
			.priceId(foods.getPriceId())
			.updatedAt(foods.getUpdatedAt())
			.discount(foods.getDiscount())
			.categoryId(foods.getCategoryId())
			.foodCategories(foods.getCategory())
			.foodVariations(foods.getFoodVariations())
			.foodImage(foods.getFoodImage())
			.foodReviews(foods.getFoodReviews())
			.build();
}

//	@Mapping(target = "imageUrl", source = "foodImage.images") // ánh xạ trực tiếp từ foodImage.images
//	FoodResponse toResponse(Foods food);
//
//	// Phương thức ánh xạ cho danh sách ảnh
//	default List<String> mapImagesToList(String images) {
//		return List.of(images.split(",")); // chuyển đổi chuỗi thành danh sách
//	}
//
//	default String mapListToImages(List<String> imageUrls) {
//		return String.join(",", imageUrls); // chuyển đổi danh sách thành chuỗi
//	}
//
//	// Ánh xạ FoodRequestDTO sang Foods
//	Foods toEntity(FoodRequest foodRequestDTO);

	public FoodResponse toResponse(Foods food) {
		FoodResponse response = new FoodResponse();
		response.setFoodId(food.getFoodId());
		response.setFoodName(food.getFoodName());
		response.setDescription(food.getDescription());
		response.setBasePrice(food.getBasePrice());
		response.setDiscount(food.getDiscount());
		response.setFoodVariations(food.getFoodVariations());
		response.setImageUrl(food.getImageUrl());
		return response;
	}
}

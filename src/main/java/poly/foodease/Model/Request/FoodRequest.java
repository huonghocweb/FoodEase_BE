package poly.foodease.Model.Request;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.FoodImage;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Response.FoodResponse;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodRequest {
	private int foodId ;
	private String foodName;
	private String description;
	private double basePrice;
	private String imageUrl;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private int discount;
	private String priceId;
	private int categoryId;
}

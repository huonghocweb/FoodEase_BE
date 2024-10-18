package poly.foodease.Model.Response;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.FoodImage;
import poly.foodease.Model.Entity.FoodReview;
import poly.foodease.Model.Entity.FoodVariations;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponse {
	private int foodId ;
	private String foodName;
	private String description;
	private double basePrice;
	private String imageUrl;
	private Date createdAt;
	private Date updatedAt;
	private int discount;
	private String priceId;
	private int categoryId;
	private List<FoodVariations> foodVariations;
	private	FoodImage foodImage;
	private List<FoodReview> foodReviews;
}

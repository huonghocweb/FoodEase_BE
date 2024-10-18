package poly.foodease.Model.Response;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.FoodSize;
import poly.foodease.Model.Entity.FoodVariationToppings;
import poly.foodease.Model.Entity.Foods;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodVariationResponse {
	private int foodVariationId;
	private String imageUrl;
	private Date createdAt;
	private Date updatedAt;
	private int quantityStock;
	private int foodId;
	private int foodSizeId;
	private Foods food;
	private FoodSize foodSize;
	private List<FoodVariationToppings> foodVariationToppings;
}

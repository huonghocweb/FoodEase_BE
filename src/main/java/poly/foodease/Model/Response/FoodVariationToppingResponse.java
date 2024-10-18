package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.FoodVariations;
import poly.foodease.Model.Entity.Toppings;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public  class FoodVariationToppingResponse {
	private int foodVariationToppingsId ;	
	
	private int foodVariationId;		
	private int toppingId;
	private FoodVariations foodVariations;
	private Toppings toppings;
}

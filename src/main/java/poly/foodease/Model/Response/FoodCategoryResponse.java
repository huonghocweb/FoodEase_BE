package poly.foodease.Model.Response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodCategoryResponse {
	
	private int categoryId;
	private String cartegoryName;
}

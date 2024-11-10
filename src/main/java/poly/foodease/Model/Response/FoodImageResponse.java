package poly.foodease.Model.Response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.Foods;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FoodImageResponse {
	private int foodsImageId;
	private String images;
	private int foodId;
	private List<Foods> foods;
}

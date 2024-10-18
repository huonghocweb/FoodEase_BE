package poly.foodease.Model.Response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.FoodVariations;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodSizeResponse {
	private int foodSizeId;
	private String foodSizeName;
	private int price;
	private List<FoodVariations> foodVariations;
}

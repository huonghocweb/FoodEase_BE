package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.FoodVariations;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Integer quantity;
    private FoodVariations foodVariation;
    private Double price;
}

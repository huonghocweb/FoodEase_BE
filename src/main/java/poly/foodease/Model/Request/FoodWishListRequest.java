package poly.foodease.Model.Request;

import lombok.Data;

@Data
public class FoodWishListRequest {
    private Integer foodVariationId;
    private Integer quantityStock;

    // Getter và Setter
    public Integer getFoodVariationId() {
        return foodVariationId;
    }

    public void setFoodVariationId(Integer foodVariationId) {
        this.foodVariationId = foodVariationId;
    }

    public Integer getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
    }
}

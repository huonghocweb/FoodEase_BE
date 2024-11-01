package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Response.FoodResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationBillDetailRequest {
    private Double price;
    private Integer quantity;
    private FoodResponse foods;
}

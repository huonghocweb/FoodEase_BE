package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationOrderDetailResponse {
    private Integer reservationOrderDetailId;
    private Double price;
    private Integer quantity;
    private FoodResponse foods;
}


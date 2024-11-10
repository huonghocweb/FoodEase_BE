package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationOrderDetailRequest {
    private Double price;
    private Integer quantity;
    private Integer foodId;
    private Integer reservationOrderId;
}

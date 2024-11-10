package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Response.ReservationOrderDetailResponse;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationOrderRequest {
    private Double totalPrice;
    private Integer totalQuantity;
    private LocalDateTime orderDatTime;
    private Boolean status;
    private List<Integer> reservationOrderDetailIds;
}

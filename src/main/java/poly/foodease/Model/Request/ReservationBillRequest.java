package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Response.ReservationBillDetailsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationBillRequest {
    private Double totalPrice;
    private Integer totalQuantity;
    private LocalDateTime paymentDateTime;
    private Boolean status;
}

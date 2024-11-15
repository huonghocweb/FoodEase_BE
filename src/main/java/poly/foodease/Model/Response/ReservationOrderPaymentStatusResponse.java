package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationOrderPaymentStatusResponse {
    private Integer reservationPaymentStatusId;
    private String reservationPaymentStatusName;
}

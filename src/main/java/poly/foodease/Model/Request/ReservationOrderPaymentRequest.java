package poly.foodease.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationOrderPaymentRequest {
    private Double totalAmount;
    private LocalDateTime paymentDateTime;
    private Integer paymentMethodId;
    private Boolean status;
    private Integer reservationOrderPaymentStatusId;
    private Integer reservationOrderId;
}

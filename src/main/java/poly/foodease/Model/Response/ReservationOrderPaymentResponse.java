package poly.foodease.Model.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.ReservationOrderPaymentStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationOrderPaymentResponse {
    private Integer reservationOrderPaymentId;
    private Double totalAmount;
    private LocalDateTime paymentDateTime;
    private PaymentMethodResponse paymentMethod;
    private ReservationOrderPaymentStatusResponse reservationOrderPaymentStatus;
    private ReservationOrderResponse reservationOrder;
}

package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationOrderResponse {
        private Integer reservationOrderId;
        private Double totalPrice;
        private Integer totalQuantity;
        private LocalDateTime orderDateTime;
        private Boolean status;
        private ReservationResponse reservation;
        private List<ReservationOrderDetailResponse> reservationOrderDetails;
}

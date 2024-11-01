package poly.foodease.Model.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import poly.foodease.Model.Entity.ReservationBillDetails;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReservationBillResponse {
    private Integer reservationBillId;
    private Double totalPrice;
    private Integer totalQuantity;
    private LocalDateTime paymentDateTime;
    private Boolean status;
    private List<ReservationBillDetailsResponse> reservationBillDetails;
}

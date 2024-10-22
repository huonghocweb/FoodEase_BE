package poly.foodease.Model.Request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {
    private Integer guests;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private Double totalDeposit;
    private Integer  reservationStatusId;
    private Integer userId;
    private Integer resTableIds;
    private List<Integer> serviceIds;
}

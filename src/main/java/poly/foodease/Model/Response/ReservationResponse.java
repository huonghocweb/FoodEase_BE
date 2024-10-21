package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {
    private Integer reservationId;
    private Integer guests;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private Double totalDeposit;
    private ReservationStatusResponse reservationStatus;
    private UserResponse user;
    private ResTableResponse resTable;
    private List<TableServicesResponse> services;
}

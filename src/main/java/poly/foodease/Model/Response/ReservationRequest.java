package poly.foodease.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationRequest {
    private Integer reservationId;
    private Integer guests;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Double totalDeposit;
    private Integer  reservationStatusId;
    private Integer userId;
    private Integer resTableIds;
    private List<Integer> serviceIds;
}

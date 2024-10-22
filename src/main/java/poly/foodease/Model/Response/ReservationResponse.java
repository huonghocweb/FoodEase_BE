package poly.foodease.Model.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationResponse {
    private Integer reservationId;
    private Integer guests;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Double totalDeposit;
    private ReservationStatusResponse reservationStatus;
    private UserResponse user;
    private ResTableResponse resTable;
    private List<TableServicesResponse> services;
}

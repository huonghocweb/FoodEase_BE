package poly.foodease.Model.Response;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class ReservationResponse {
    private Integer reservationId;
    private Integer guests;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private LocalDateTime bookTime;
    private Double totalDeposit;
    private String checkinCode;
    private ReservationStatusResponse reservationStatus;
    private UserResponse user;
    private ResTableResponse resTable;
    private List<TableServicesResponse> services;
    private List<FoodResponse> foods;
}

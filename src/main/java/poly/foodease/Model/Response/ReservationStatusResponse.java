package poly.foodease.Model.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationStatusResponse {
    private Integer reservationStatusId;
    private String reservationStatusName;
}

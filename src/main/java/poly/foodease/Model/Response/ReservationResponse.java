package poly.foodease.Model.Response;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Integer reservationId;
    private String status; // "Pending", "Accepted", "Cancelled"
    private String name;
    private String email;
    private String phone;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Integer guests;
    private Integer tableId; // ID của bàn được đặt
    private String tableName;
}

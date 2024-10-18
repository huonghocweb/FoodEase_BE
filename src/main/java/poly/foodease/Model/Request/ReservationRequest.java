package poly.foodease.Model.Request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private String name;
    private String email;
    private String phone;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Integer guests;
    private Integer tableId; // ID của bàn được đặt
    private String tableName; 
}

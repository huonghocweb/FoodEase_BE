package poly.foodease.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="reservation_payment_status")
public class ReservationOrderPaymentStatus {
    @Id
    @Column(name = "reservation_payment_status_id")
    private Integer reservationPaymentStatusId;

    @Column(name="reservation_payment_status_name")
    private String reservationPaymentStatusName;
}

package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "reservation_bill")
public class ReservationBill {
        @Id
        @Column(name="reservation_bill_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer reservationBillId;

    @Column(name="total_price")
    private Double totalPrice;

    @Column(name="total_quantity")
    private Integer totalQuantity;

    @Column(name="payment_datetime")
    private LocalDateTime paymentDateTime;

    @Column(name="status")
    private Boolean status;

    @JsonIgnore
    @OneToMany(mappedBy = "reservationBill")
    private List<ReservationBillDetails> reservationBillDetails;
}

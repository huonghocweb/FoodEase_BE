package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="reservation_payment")
public class ReservationOrderPayment {
    @Id
    @Column(name="reservation_payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationOrderPaymentId;

    @Column(name="total_amount")
    private Double totalAmount;

    @Column(name="payment_datetime")
    private LocalDateTime paymentDateTime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reservation_order_id")
    private ReservationOrder reservationOrder;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="payment_id")
    private PaymentMethod paymentMethod;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reservation_payment_status_id")
    private ReservationOrderPaymentStatus reservationPaymentStatus;
}

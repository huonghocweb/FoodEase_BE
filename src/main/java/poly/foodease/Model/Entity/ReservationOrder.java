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
@Table(name= "reservation_order")
public class ReservationOrder {
    @Id
    @Column(name="reservation_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationOrderId;

    @Column(name="total_price")
    private Double totalPrice;

    @Column(name="total_quantity")
    private Integer totalQuantity;

    @Column(name="order_datetime")
    private LocalDateTime orderDateTime;

    @Column(name="status")
    private Boolean status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    @JsonIgnore
    @OneToMany(mappedBy = "reservationOrder")
    private List<ReservationOrderDetail> reservationOrderDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "reservationOrder")
    private List<ReservationOrderPayment> reservationOrderPayments;

}

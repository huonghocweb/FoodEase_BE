package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "reservation_order_detail")
public class ReservationOrderDetail {
    @Id
    @Column(name="reservation_order_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationOrderDetailId;

    @Column(name="price")
    private Double price;

    @Column(name="quantity")
    private Integer quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reservation_order_id")
    private ReservationOrder reservationOrder;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="food_id")
    private Foods foods;
}

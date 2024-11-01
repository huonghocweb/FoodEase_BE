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
@Table(name= "reservation_bill_detail")
public class ReservationBillDetails {
    @Id
    @Column(name="reservation_bill_detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationBillDetailId;

    @Column(name="price")
    private Double price;

    @Column(name="quantity")
    private Integer quantity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reservation_bill_id")
    private ReservationBill reservationBill;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="food_id")
    private Foods foods;
}

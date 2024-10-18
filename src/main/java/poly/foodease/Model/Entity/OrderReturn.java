package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="order_return")
public class OrderReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_return_id")
    private Integer orderReturnId;

    @Column(name="return_date_time")
    private LocalDateTime returnDateTime;

    @Column(name="reason")
    private String reason;

    @Column(name="status")
    private String status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="food_variation_id")
    private FoodVariations foodVariations;

}

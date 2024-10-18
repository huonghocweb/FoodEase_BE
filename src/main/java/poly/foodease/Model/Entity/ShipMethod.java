package poly.foodease.Model.Entity;

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
@Table(name="ship_method")
public class ShipMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ship_id")
    private Integer shipId;

    @Column(name="ship_name")
    private String shipName;

    @Column(name="ship_duration")
    private Integer shipDuration;

    @Column(name="ship_fee")
    private Integer shipFee;

    @Column(name="ship_status")
    private Boolean shipStatus;

    @Column(name ="ship_description")
    private String shipDescription;

    @Column(name="create_at")
    private LocalDateTime createAt;

    @Column(name="update_at")
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "shipMethod")
    private List<Order> orders;
}

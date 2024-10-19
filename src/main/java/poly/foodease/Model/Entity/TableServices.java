package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="table_services")
public class TableServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="service_id")
    private Integer serviceId;

    @Column(name="service_name")
    private String serviceName;

    @Column(name="service_price")
    private Double servicePrice;

    @Column(name="description")
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "services")
    private List<Reservation> reservations;

}

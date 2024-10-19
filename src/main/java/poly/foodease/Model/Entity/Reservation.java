package poly.foodease.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_id")
    private Integer reservationId;

    @Column(name="reservation_id")
    private Integer guests;

    @Column(name="reservation_id")
    private LocalDate reservationDate;

    @Column(name="reservation_id")
    private LocalTime reservationTime;

    @Column(name="total_deposit")
    private Double totalDeposit;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reservation_status_id")
    private ReservationStatus reservationStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="table_id")
    private ResTable resTable;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "reservation_service",
            joinColumns = @JoinColumn(name="reservation_id"),
            inverseJoinColumns = @JoinColumn(name="service_id")
    )
    private List<TableServices> services;

}

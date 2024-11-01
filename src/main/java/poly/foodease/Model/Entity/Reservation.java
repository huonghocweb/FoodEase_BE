package poly.foodease.Model.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name="guests")
    private Integer guests;

    @Column(name="checkin_time")
    private LocalDateTime checkinTime;

    @Column(name="checkout_time")
    private LocalDateTime checkoutTime;

    @Column(name = "book_time")
    private LocalDateTime bookTime;

    @Column(name="total_deposit")
    private Double totalDeposit;

    @Column(name="checkin_code")
    private String checkinCode;

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

//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(
//            name = "reservation_food",
//            joinColumns = @JoinColumn(name = "reservation_id"),
//            inverseJoinColumns = @JoinColumn(name = "food_id")
//    )
//    private List<Foods>  foods;

}

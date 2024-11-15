package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.ReservationOrder;

import java.util.Optional;

public interface ReservationOrderRepo extends JpaRepository<ReservationOrder , Integer> {

    @Query("SELECT resO FROM ReservationOrder resO JOIN resO.reservation res " +
            " WHERE res.reservationId = :reservationId")
    ReservationOrder getReservationOrderByReservationId(@Param("reservationId") Integer reservationId);
}

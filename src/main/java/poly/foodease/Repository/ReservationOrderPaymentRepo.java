package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.ReservationOrderPayment;

public interface ReservationOrderPaymentRepo extends JpaRepository<ReservationOrderPayment, Integer> {

    @Query("SELECT resOP FROM ReservationOrderPayment resOP JOIN resOP.reservationOrder.reservation  res " +
            " WHERE res.reservationId = :reservationId")
    ReservationOrderPayment getReservationOrderPaymentByReservationId(@Param("reservationId") Integer reservationId);
}

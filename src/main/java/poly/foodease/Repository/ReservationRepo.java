package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    @Query("SELECT res FROM Reservation res JOIN res.user u WHERE u.userName = :userName")
    Optional<Reservation> getReservationByReservationByUserName(@Param("userName") String userName);

    @Query("SELECT res FROM Reservation res JOIN res.resTable rtb JOIN res.reservationStatus resSta " +
            "WHERE rtb.tableId = :tableId  AND resSta.reservationStatusId = 1 " +
            "AND res.checkinTime >= :startOfDay AND res.checkinTime < :endOfDay " )
    List<Reservation> getReservationsByTableIdAndDate(
            @Param("tableId") Integer tableId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);
}

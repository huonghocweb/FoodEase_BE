package poly.foodease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import poly.foodease.Model.Entity.Reservation;

import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Integer> {
    @Query("SELECT re FROM Reservation re JOIN re.user u WHERE u.userName = :userName")
    Optional<Reservation> getReservationByReservationByUserName(@Param("userName") String userName);
}
